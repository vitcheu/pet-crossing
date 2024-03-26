package com.vitcheu.common.model.combat.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.vitcheu.common.model.combat.MesageDataHandler;
import com.vitcheu.common.model.combat.MessageType;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "dataType"
)
@JsonSubTypes(
  {
    @Type(value = TurnAction.class, name = "TurnAction_type"),
    @Type(value = UsePropsAction.class, name = "PropuseAction_type"),
    @Type(value = TurnResult.class, name = "TurnResult_type"),
    @Type(value = ConnectRequest.class, name = "ConnectRequest_type"),
    @Type(value = ServerRequest.class, name = "ServerRequest_type"),
    @Type(value = ClientResponse.class, name = "ClientResponse_type"),
    @Type(value = ServerMessage.class,name = "ServerMessage_type")
  }
)
public interface CombatData {
  MessageType getType();

  void accept(MesageDataHandler handler);
}
