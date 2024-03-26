package com.vitcheu.common.model.combat.messages;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vitcheu.common.model.combat.MesageDataHandler;
import com.vitcheu.common.model.combat.MessageType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServerRequest<T> extends AbstactCombatData {

  @JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@class"
  )
  private T requestBody;

  public ServerRequest(T requestBody) {
    super(MessageType.SERVER_REQUEST);
    this.requestBody = requestBody;
  }

  private ServerRequest() {
    super(MessageType.SERVER_REQUEST);
  }

  @Override
  public void accept(MesageDataHandler handler) {
    handler.handle(this);
  }
}
