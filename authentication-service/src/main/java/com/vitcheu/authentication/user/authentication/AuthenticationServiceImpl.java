package com.vitcheu.authentication.user.authentication;

import static com.vitcheu.common.constants.redis.RedisContants.AUTH_KEY_PREFIX;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vitcheu.authentication.mail.EmailService;
import com.vitcheu.authentication.redis.RedisClient;
import com.vitcheu.authentication.security.jwt.model.*;
import com.vitcheu.authentication.security.jwt.repository.VerificationTokenRepository;
import com.vitcheu.authentication.security.jwt.service.JwtProvider;
import com.vitcheu.authentication.security.jwt.service.RefreshTokenService;
import com.vitcheu.authentication.user.UserPrincipal;
import com.vitcheu.authentication.user.model.*;
import com.vitcheu.authentication.user.repository.UserRepository;
import com.vitcheu.authentication.web.client.OwnerClient;
import com.vitcheu.common.exception.EmailSendException;
import com.vitcheu.common.exception.OtherExceptions;
import com.vitcheu.common.model.AuthenticationToken;
import com.vitcheu.common.model.email.Email;
import com.vitcheu.common.model.request.AddUserRequest;
import com.vitcheu.common.utils.Settings;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

  private final ModelMapper modelMapper;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final VerificationTokenRepository verificationTokenRepository;
  private final EmailService mailService;
  private final RefreshTokenService refreshTokenService;
  private final Settings settings;
  // @Autowired
  private final JwtProvider jwtProvider;
  // @Autowired
  private final AuthenticationManager authenticationManager;
  private final RedisClient redisClient;
  private final OwnerClient ownerClient;

  @Value("${mail.body}")
  private String mailBody;

  public AuthenticationServiceImpl(
    ModelMapper modelMapper,
    UserRepository userRepository,
    PasswordEncoder passwordEncoder,
    VerificationTokenRepository verificationTokenRepository,
    EmailService mailService,
    RefreshTokenService refreshTokenService,
    Settings settings,
    JwtProvider jwtProvider,
    AuthenticationManager authenticationManager,
    RedisClient redisClient,
    OwnerClient ownerClient
  ) {
    this.modelMapper = modelMapper;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.verificationTokenRepository = verificationTokenRepository;
    this.mailService = mailService;
    this.refreshTokenService = refreshTokenService;
    this.settings = settings;
    this.jwtProvider = jwtProvider;
    this.authenticationManager = authenticationManager;
    this.redisClient = redisClient;
    this.ownerClient = ownerClient;
  }

  @Override
  @Transactional
  public void signup(UserSignup userSignup) {
    AuthenticationServiceImpl.log.info(
      "-----> AuthenticationServiceImpl signup"
    );

    // this.settings.loadApplicationSettings();

    User user;

    user = this.modelMapper.map(userSignup, User.class);

    user.setPassword(this.passwordEncoder.encode(userSignup.getPassword()));
    user.setEnabled(false);
    user.setAccountNonExpired(false);
    user.setAccountNonLocked(false);
    user.setCredentialsNonExpired(false);

    Role role = new Role();
    role.setName("ROLE_PERSON");

    List<Role> list = new ArrayList<>();
    list.add(role);

    Permission permission = new Permission();
    permission.setName("PERSON_READ");
    List<Permission> permissions = new ArrayList<>();
    permissions.add(permission);

    role.setPermissions(permissions);
    user.setRoles(list);

    this.userRepository.save(user);

    String token = this.generateVerificationToken(user);

    try {
      this.mailService.sendMail(
          new Email(
            this.settings.getMailSubject(),
            user.getEmail(),
            this.mailBody +
            token +
            " This link is valid for the next " +
            this.settings.getVerificationTokenValidity().toString() +
            " minute.",
            this.settings.getMailReplyTo()
          )
        );
    } catch (EmailSendException e) {
      throw new OtherExceptions("Error sending email");
    }

    boolean added = this.ownerClient.addUser(new AddUserRequest(user.getId()));
    if (!added) {
      throw new OtherExceptions("Error adding user");
    }
    log.info("User added successfully");
  }

  @Override
  public void fetchUserAndEnable(VerificationToken verificationToken) {
    var id = verificationToken.getUserId();

    User user =
      this.userRepository.findById(id)
        .orElseThrow(() -> new OtherExceptions("User does not exist"));

    user.setEnabled(true);
    user.setAccountNonExpired(true);
    user.setAccountNonLocked(true);
    user.setCredentialsNonExpired(true);

    this.userRepository.save(user);
  }

  @Override
  @Transactional
  public String verifyAccount(String token) {
    Optional<VerificationToken> verificationToken =
      this.verificationTokenRepository.findById(token);

    if (
      verificationToken.isPresent() &&
      verificationToken
        .get()
        .getStatus()
        .equals(VerificationTokenEnum.UNVERIFIED) &&
      Instant.now().isBefore(verificationToken.get().getExpiryDate())
    ) {
      verificationToken.get().setStatus(VerificationTokenEnum.VERIFIED);

      VerificationToken vtoken = verificationToken.get();
      this.verificationTokenRepository.save(vtoken);
      this.fetchUserAndEnable(vtoken);
    } else if (
      verificationToken.isPresent() &&
      verificationToken.get().getStatus().equals(VerificationTokenEnum.VERIFIED)
    ) {
      return "Account already verified.";
    } else if (!verificationToken.isPresent()) {
      return "invalid verification token, please check the verification link.";
    } else {
      this.verificationTokenRepository.deleteById(
          verificationToken.get().getToken()
        );
      this.userRepository.deleteById(verificationToken.get().getUserId());

      return "token expired! Please register again.";
    }

    verificationTokenRepository.deleteById(verificationToken.get().getToken());
    return "Successfully verified account. Please login to continue.";
  }

  @Override
  public String generateVerificationToken(User user) {
    String token = UUID.randomUUID().toString();

    VerificationToken verificationToken = new VerificationToken();

    verificationToken.setToken(token);
    verificationToken.setUserId(user.getId());
    verificationToken.setExpiryDate(
      Instant
        .now()
        .plus(this.settings.getVerificationTokenValidity(), ChronoUnit.MINUTES)
    );
    verificationToken.setStatus(VerificationTokenEnum.UNVERIFIED);

    this.verificationTokenRepository.save(verificationToken);

    return token;
  }

  @Override
  @Transactional
  public AuthenticationResponse login(UserLogin userLogin) {
    Authentication authentication =
      this.authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
            userLogin.getUsername(),
            userLogin.getPassword()
          )
        );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    /*Also save to Redis */
    saveToRedis(authentication);

    var expiration = Instant
      .now()
      .plusSeconds(this.settings.getJwtExpirationTime());
    String token = this.jwtProvider.generateToken(authentication, expiration);

    return AuthenticationResponse
      .builder()
      .authenticationToken(token)
      .refreshToken(
        this.refreshTokenService.generateRefreshToken(
            "LOGIN",
            userLogin.getUsername()
          )
          .getToken()
      )
      .expiresAt(
        ZonedDateTime.ofInstant(expiration, ZoneId.of("Asia/Shanghai"))
      )
      .username(userLogin.getUsername())
      .build();
  }

  @Override
  public AuthenticationResponse refreshToken(RefreshToken refreshToken) {
    this.refreshTokenService.validateRefreshToken(refreshToken);

    String token =
      this.jwtProvider.generateTokenWithUserName(refreshToken.getUsername());

    return AuthenticationResponse
      .builder()
      .authenticationToken(token)
      .refreshToken(
        this.refreshTokenService.generateRefreshToken(
            "POST_LOGIN",
            refreshToken.getUsername()
          )
          .getToken()
      )
      .expiresAt(
        ZonedDateTime
          .now()
          .plus(this.settings.getJwtExpirationTime(), ChronoUnit.MILLIS)
      )
      .username(refreshToken.getUsername())
      .build();
  }

  private void saveToRedis(Authentication authentication) {
    var priciple = ((UserPrincipal) (authentication.getPrincipal()));
    Long userId = priciple.getUser().getId();

    var auth_token = AuthenticationToken
      .builder()
      .name(authentication.getName())
      .userId(userId)
      .build();
    log.info("Saving auth token: " + auth_token);

    redisClient.set(
      AUTH_KEY_PREFIX + authentication.getName(),
      auth_token,
      settings.getJwtExpirationTime(),
      TimeUnit.SECONDS
    );
  }
}
