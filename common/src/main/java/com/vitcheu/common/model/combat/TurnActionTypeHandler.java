package com.vitcheu.common.model.combat;

public interface TurnActionTypeHandler {
  default void handleAttack(ActionInfo info) {}

  default void handleDefence(ActionInfo info) {}

  default void handleProps(ActionInfo info) {}
}
