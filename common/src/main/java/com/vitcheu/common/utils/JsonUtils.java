package com.vitcheu.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static String toJsonStr(Object value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T toObject(String string, Class<T> clazz) {
    T value;
    try {
      value = objectMapper.readValue(string, clazz);
      return value;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
