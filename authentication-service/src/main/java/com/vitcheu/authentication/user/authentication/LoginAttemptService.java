package com.vitcheu.authentication.user.authentication;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class LoginAttemptService {

  //max login fail times
  private static final int MAX_ATTEMPT = 5;

  //duration of account blocking
  private static final int ACCOUNT_BLOCK_DURATION = 3;

  //number of wrong try
  private LoadingCache<String, Integer> attemptsCache;

  public LoginAttemptService() {
    super();
    this.attemptsCache =
      CacheBuilder
        .newBuilder()
        .expireAfterWrite(
          LoginAttemptService.ACCOUNT_BLOCK_DURATION,
          TimeUnit.MINUTES
        )
        .build(
          new CacheLoader<String, Integer>() {
            @Override
            public Integer load(final String key) {
              return 0;
            }
          }
        );
  }

  //Successful authentication resets the unsuccessful login attempts counter
  public void loginSucceeded(final String key) {
    LoginAttemptService.log.info("-----> LoginAttemptService loginSucceeded");

    this.attemptsCache.invalidate(key);
  }

  //Unsuccessful authentication attempt increases the number of attempts for that IP
  public void loginFailed(final String key) {
    LoginAttemptService.log.info("-----> LoginAttemptService loginFailed");

    int attempts = 0;

    try {
      attempts = this.attemptsCache.get(key);
    } catch (final ExecutionException e) {
      attempts = 0;
    }

    attempts++;

    LoginAttemptService.log.info(
      "Unsuccessful Login Attempt : " +
      attempts +
      "/" +
      LoginAttemptService.MAX_ATTEMPT
    );

    this.attemptsCache.put(key, attempts);
  }

  public boolean isBlocked(final String key) {
    LoginAttemptService.log.info("-----> LoginAttemptService isBlocked");

    try {
      return this.attemptsCache.get(key) >= LoginAttemptService.MAX_ATTEMPT;
    } catch (final ExecutionException e) {
      return false;
    }
  }
}
