package com.vitcheu.combat.context;

import java.util.Objects;
import org.springframework.context.ApplicationContext;

public class AppContextHolder {

  private static ApplicationContext appContext;

  public static ApplicationContext getAppContext() {
    return appContext;
  }

  public static void setAppContext(ApplicationContext appContext) {
    AppContextHolder.appContext = appContext;
  }

  public static <T> T getBean(Class<T> clazz) {
    var bean = appContext.getBean(clazz);
    return Objects.requireNonNull(bean);
  }
}
