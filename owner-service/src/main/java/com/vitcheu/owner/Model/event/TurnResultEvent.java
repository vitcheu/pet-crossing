package com.vitcheu.owner.model.event;

import com.vitcheu.common.model.combat.messages.TurnResult;

import lombok.Getter;

public final class TurnResultEvent extends CombatEvent {
  @Getter
  private TurnResult result;

  public TurnResultEvent(long userId,TurnResult result) {
    super(userId);
    this.result=result;
  }

}
