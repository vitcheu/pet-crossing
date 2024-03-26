package com.vitcheu.common.model.combat.messages;

import com.vitcheu.common.model.combat.MesageDataHandler;
import com.vitcheu.common.model.combat.MessageType;
import com.vitcheu.common.model.response.PropUsedResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClientResponse extends AbstactCombatData {

  private PropUsedResult propUsedResult;

  public ClientResponse(PropUsedResult propUsedResult) {
    super(MessageType.CLIENT_RESPONSE);
    this.propUsedResult = propUsedResult;
  }

  private ClientResponse() {
    super(MessageType.CLIENT_RESPONSE);
  }

  @Override
  public void accept(MesageDataHandler handler) {}
}
