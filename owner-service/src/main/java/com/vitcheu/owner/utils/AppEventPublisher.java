package com.vitcheu.owner.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

import com.vitcheu.owner.context.AppContextHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppEventPublisher {

  public static void publishEvent(ApplicationEvent event) {
    log.info("publishing event:"+event);
    AppContextHolder.getContext().publishEvent(event);
  }
}
