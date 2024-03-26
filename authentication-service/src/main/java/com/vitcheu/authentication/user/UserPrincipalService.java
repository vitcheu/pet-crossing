package com.vitcheu.authentication.user;

import com.vitcheu.authentication.user.authentication.LoginAttemptService;
import com.vitcheu.authentication.user.model.User;
import com.vitcheu.authentication.user.repository.UserRepository;
import com.vitcheu.common.exception.FailedLoginAttemptException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserPrincipalService implements UserDetailsService {

  @Autowired
  UserRepository appUserRepository;

  @Autowired
  private LoginAttemptService loginAttemptService;

  @Autowired
  private HttpServletRequest request;

  @Override
  public UserDetails loadUserByUsername(String username) {
    UserPrincipalService.log.info("-----> loadUserByUsername  : " + username);

    String ip = this.getClientIP();

    if (this.loginAttemptService.isBlocked(ip)) {
      UserPrincipalService.log.info(
        "-----> Your IP has been blocked for 2 minute for 2 consecutive failed login attempts."
      );

      throw new FailedLoginAttemptException();
    }

    User user =
      this.appUserRepository.findByUsername(username)
        .orElseThrow(() ->
          new UsernameNotFoundException(
            String.format("Username %s not found", username)
          )
        );

    return new UserPrincipal(user);
  }

  private String getClientIP() {
    final String xfHeader = this.request.getHeader("X-Forwarded-For");

    if (xfHeader != null) {
      return xfHeader.split(",")[0];
    }

    UserPrincipalService.log.info(
      "-----> getClientIP  : " + this.request.getRemoteAddr()
    );

    return this.request.getRemoteAddr();
  }
}
