package com.vitcheu.combat.utils;

import org.springframework.context.ApplicationEvent;

import com.vitcheu.combat.context.AppContextHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppEventPublisher {

  public static void publishEvent(ApplicationEvent event) {
    log.info("publishing event:" + event);
    AppContextHolder.getAppContext().publishEvent(event);
  }
}
