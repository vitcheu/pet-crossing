package com.vitcheu.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Converter {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static <T> T convert(Object value, Class<T> toType) {
    T convertValue = objectMapper.convertValue(value, toType);
    return convertValue;
  }
}
