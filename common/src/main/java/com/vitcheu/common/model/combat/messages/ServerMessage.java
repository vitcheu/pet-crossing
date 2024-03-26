package com.vitcheu.common.model.combat.messages;

import com.vitcheu.common.model.combat.MesageDataHandler;
import com.vitcheu.common.model.combat.MessageType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServerMessage extends AbstactCombatData {

  private String text;

  public ServerMessage(String text) {
    super(MessageType.SERVER_MESSAGE);
    this.text = text;
  }

  private ServerMessage() {
    super(MessageType.SERVER_MESSAGE);
  }

  @Override
  public void accept(MesageDataHandler handler) {
    handler.handle(this);
  }
}
