package com.vitcheu.combat.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import com.vitcheu.common.model.combat.messages.CombatMessage;

public class SendSocketMessageRequestEvent extends ApplicationEvent {
  @Getter
  private long userId;

  @Getter
  private final CombatMessage message;

  public SendSocketMessageRequestEvent(CombatMessage message,long userId) {
    super(message);
    this.message = message;
    this.userId=userId;
  }
}
