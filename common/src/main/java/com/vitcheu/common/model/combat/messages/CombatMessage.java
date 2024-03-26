package com.vitcheu.common.model.combat.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vitcheu.common.model.combat.MessageType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CombatMessage {

  CombatData payload;

  public static CombatMessage conncetRequestMessage(long to, int usedPet) {
    return new CombatMessage(new ConnectRequest(to, usedPet, true));
  }

  public static CombatMessage actionMessage(TurnAction action) {
    return new CombatMessage(action);
  }

  public static CombatMessage actionMessage(TurnActionType type) {
    return new CombatMessage(new TurnAction(type));
  }

  public static CombatMessage serverRequest(ServerRequest request) {
    return new CombatMessage(request);
  }

  public static CombatMessage clientResponse(ClientResponse response) {
    return new CombatMessage(response);
  }

  public static CombatMessage turnResult(TurnResult result) {
    return new CombatMessage(result);
  }

  public static CombatMessage serverMessage(String text) {
    return new CombatMessage(
      new ServerMessage(text)
    );
  }

  @JsonIgnore
  public MessageType getMessageType() {
    return payload.getType();
  }
}
