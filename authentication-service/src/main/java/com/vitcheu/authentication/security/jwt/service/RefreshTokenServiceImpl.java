package com.vitcheu.authentication.security.jwt.service;

import com.vitcheu.authentication.redis.RedisClient;
import com.vitcheu.authentication.security.jwt.model.RefreshToken;
import com.vitcheu.authentication.security.jwt.repository.RefreshTokenRepository;
import com.vitcheu.common.exception.OtherExceptions;

import static com.vitcheu.common.constants.redis.RedisContants.*;

import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Log4j2
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final RedisClient redisClient;


  @Override
  @CachePut(cacheNames = CACHE_NAME_REFRESH_TOKEN, key = CACHE_AUTH_KEY)
  public RefreshToken generateRefreshToken(String stage, String username) {
    var refreshToken = this.refreshTokenRepository.findByUsername(username);
    var rToken=refreshToken.orElse(new RefreshToken());

    if (stage.equalsIgnoreCase("LOGIN")) {
      RefreshTokenServiceImpl.log.info("LOGIN token generation");
    } else if (stage.equalsIgnoreCase("POST_LOGIN")) {
      RefreshTokenServiceImpl.log.info("POST_LOGIN token generation");
        refreshToken
          .orElseThrow(() -> new OtherExceptions("Invalid Refresh Token"));
    }
    
    rToken.setToken(UUID.randomUUID().toString());
    rToken.setUsername(username);

    return this.refreshTokenRepository.save(rToken);
  }

  @Override
  public void validateRefreshToken(RefreshToken refreshToken) {
    RefreshTokenServiceImpl.log.info("Validating Refresh Token");

    RefreshToken rToken = getRefreshTokenByUserName(refreshToken.getUsername());
    if (rToken == null || !refreshToken.getToken().equals(rToken.getToken())) {
      throw new OtherExceptions("Invalid Refresh Token");
    }
  }

  @Cacheable(cacheNames = CACHE_NAME_REFRESH_TOKEN, key = CACHE_AUTH_KEY)
  public RefreshToken getRefreshTokenByUserName(String username) {
    RefreshTokenServiceImpl.log.info(
      "Get RefreshToken By UserName: " + username
    );

    Optional<RefreshToken> opt_token = refreshTokenRepository.findByUsername(
      username
    );
    if (opt_token.isEmpty()) return null;
    return opt_token.get();
  }


  @Override
  @CacheEvict(cacheNames = CACHE_NAME_REFRESH_TOKEN, key = CACHE_AUTH_KEY)
  public void deleteByToken(String token, String username) {
    RefreshToken refreshToken =
      this.refreshTokenRepository.findByToken(token)
        .orElseThrow(() -> new OtherExceptions("Invalid Refresh Token"));

    if (
      refreshToken.getToken().equals(token) &&
      refreshToken.getUsername().equals(username)
    ) {
      this.refreshTokenRepository.deleteByToken(token);
      /* delete auth token stored in redis */
      this.redisClient.delete(CACHE_NAME_AUTH, username); 

      RefreshTokenServiceImpl.log.info("Token deletion successful.");
    } else {
      RefreshTokenServiceImpl.log.info("Token, Username mismatch!");

      throw new OtherExceptions("Token, Username mismatch!");
    }
  }
}
