package com.vitcheu.common.utils;

import java.util.Optional;

public class TypeCaster {

  public static <T> Optional<T> safeCast(Object obj, Class<T> targetType) {
    if (targetType.isInstance(obj)) {
      return Optional.of(targetType.cast(obj));
    } else {
      return Optional.empty();
    }
  }

  public static <T> T cast(Object obj, Class<T> targetType) {
    if (targetType.isInstance(obj)) {
      return targetType.cast(obj);
    } else {
      throw new ClassCastException(
        obj.getClass().getName() + " cannot be cast to " + targetType.getName()
      );
    }
  }
}
