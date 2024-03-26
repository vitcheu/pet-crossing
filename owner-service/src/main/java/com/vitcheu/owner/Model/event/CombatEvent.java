package com.vitcheu.owner.model.event;

public abstract sealed class CombatEvent
  extends GameEvent
  permits CombatReadyEvent, TurnResultEvent {

  public CombatEvent(long userId) {
    super(userId);
  }

  public long getUserId() {
    return (long) (getSource());
  }
}
