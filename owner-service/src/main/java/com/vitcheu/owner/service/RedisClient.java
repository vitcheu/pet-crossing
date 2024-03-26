package com.vitcheu.owner.service;

import static com.vitcheu.common.constants.redis.RedisContants.DELEMITOR;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import com.vitcheu.common.utils.JsonUtils;

import io.jsonwebtoken.lang.Strings;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RedisClient {

  private final StringRedisTemplate stringRedisTemplate;

  public void set(String cacheName, String key, @Nullable Object value) {
    if (value == null) {
      return;
    }
    stringRedisTemplate
      .opsForValue()
      .set(cacheName + DELEMITOR + key, JsonUtils.toJsonStr(value));
  }

  public void delete(String cacheName, String key) {
    stringRedisTemplate.delete(cacheName + DELEMITOR + key);
  }

  public @Nullable <T> T get(String cacheName, String key, Class<T> clazz) {
    String string = stringRedisTemplate
      .opsForValue()
      .get(cacheName + DELEMITOR + key);
    if (!Strings.hasLength(string)) {
      return null;
    }
    return JsonUtils.toObject(string, clazz);
  }
}
