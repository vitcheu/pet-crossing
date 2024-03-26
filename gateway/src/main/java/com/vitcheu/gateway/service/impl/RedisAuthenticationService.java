package com.vitcheu.gateway.service.impl;

import com.vitcheu.common.constants.redis.RedisContants;
import com.vitcheu.common.model.AuthenticationToken;
import com.vitcheu.gateway.redis.RedisClient;
import com.vitcheu.gateway.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisAuthenticationService implements AuthenticationService {

  @Autowired
  RedisClient redisClient;

  @Override
  public AuthenticationToken loadAuthentication(String username) {
    var token = redisClient.get(
      RedisContants.AUTH_KEY_PREFIX + username,
      AuthenticationToken.class
    );
    log.info("authentication: " + token);
    return token;
  }
}
