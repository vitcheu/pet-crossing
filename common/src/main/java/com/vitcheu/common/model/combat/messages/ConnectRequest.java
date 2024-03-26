package com.vitcheu.common.model.combat.messages;

import com.vitcheu.common.model.combat.MesageDataHandler;
import com.vitcheu.common.model.combat.MessageType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString(callSuper = true)
public class ConnectRequest extends AbstactCombatData {

  long to;

  int petId;
  /* indicates connect request or disconnect request */
  boolean connect;

  public ConnectRequest(long to, int petId, boolean connect) {
    super(MessageType.CONNECT);
    this.to = to;
    this.petId = petId;
    this.connect = connect;
  }

  private ConnectRequest(){
    super(MessageType.CONNECT);
  }

  @Override
  public void accept(MesageDataHandler handler) {
    handler.handle(this);
  }
}
