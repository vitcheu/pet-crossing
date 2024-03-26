package com.vitcheu.common.model.combat.messages;

import com.vitcheu.common.model.combat.MesageDataHandler;
import com.vitcheu.common.model.combat.MessageType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@EqualsAndHashCode(callSuper = true)
public class TurnAction extends AbstactCombatData {

  TurnActionType actionType;

  public TurnAction(TurnActionType actionType) {
    super(MessageType.COMBAT);
    this.actionType = actionType;
  }

  private TurnAction() {
    super(MessageType.COMBAT);
  }

  @Override
  public void accept(MesageDataHandler handler) {
    handler.handle(this);
  }
}
