package com.vitcheu.owner.context;

import java.util.Objects;

import org.springframework.context.ApplicationContext;

public class AppContextHolder {

  private static ApplicationContext context;

  public static ApplicationContext getContext() {
    return Objects.requireNonNull(context);
  }

  public static void setContext(ApplicationContext context) {
    AppContextHolder.context = Objects.requireNonNull(context);
  }

  public static <T> T getBean(Class<T> clazz) {
    return getContext().getBean(clazz);
  }
}
