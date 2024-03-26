package com.vitcheu.gateway.redis;

import java.util.concurrent.TimeUnit;

import org.bouncycastle.jcajce.provider.asymmetric.dsa.DSASigner.stdDSA;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.vitcheu.common.utils.JsonUtils;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RedisClient {

  private final StringRedisTemplate stringRedisTemplate;

  public void set(String key, Object value) {
    stringRedisTemplate.opsForValue().set(key, JsonUtils.toJsonStr(value));
  }

  public void set(String key, Object value, Long time, TimeUnit unit) {
    stringRedisTemplate
      .opsForValue()
      .set(key, JsonUtils.toJsonStr(value), time, unit);
  }

  public <T> T get(String key,Class<T> clazz){
    String string = stringRedisTemplate.opsForValue().get(key);
    return  JsonUtils.toObject(string,clazz);
  }
}
