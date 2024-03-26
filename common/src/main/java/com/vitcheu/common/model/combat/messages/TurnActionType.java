package com.vitcheu.common.model.combat.messages;

import com.vitcheu.common.model.combat.ActionInfo;
import com.vitcheu.common.model.combat.TurnActionTypeHandler;

public enum TurnActionType {
  ATTACK(20) {
    @Override
    public void accpet(TurnActionTypeHandler handler, ActionInfo info) {
      handler.handleAttack(info);
    }
  },

  DEFENSE(30) {
    @Override
    public void accpet(TurnActionTypeHandler handler, ActionInfo info) {
      handler.handleDefence(info);
    }
  },

  /*using props action */
  PROPS(20) {
    @Override
    public void accpet(TurnActionTypeHandler handler, ActionInfo info) {
      handler.handleProps(info);
    }
  };

  private final int priority;

  TurnActionType(int priority) {
    this.priority = priority;
  }

  public int getPriority() {
    return priority;
  }

  public static boolean isAttack(TurnActionType action) {
    return action.equals(ATTACK);
  }

  public abstract void accpet(TurnActionTypeHandler handler, ActionInfo info);
}
