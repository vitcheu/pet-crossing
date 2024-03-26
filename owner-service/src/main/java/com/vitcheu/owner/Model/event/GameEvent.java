package com.vitcheu.owner.model.event;

import org.springframework.context.ApplicationEvent;

public class GameEvent extends ApplicationEvent {

  public GameEvent(Object source){
    super(source);
  }
}
