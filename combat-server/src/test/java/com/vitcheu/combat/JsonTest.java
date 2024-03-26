package com.vitcheu.combat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitcheu.common.model.PetPropertiesName;
import com.vitcheu.common.model.combat.*;
import com.vitcheu.common.model.combat.messages.*;
import com.vitcheu.common.model.request.UsePropsRequest;
import com.vitcheu.common.model.response.PropUsedResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTest {

  private ObjectMapper objectMapper;

  @BeforeEach
  void init() {
    objectMapper = new ObjectMapper();
  }

  @Test
  void testEnumJson() {
    CombatMessage message = CombatMessage.actionMessage(TurnActionType.ATTACK);

    // TurnResult turnResult = new TurnResult(PetPropertiesName.HP, -10);

    // var resultMsg = new CombatMessage(MessageType.RESULT, turnResult);
    UsePropsAction usePropsAction = new UsePropsAction(1, 10);
    CombatMessage userPropsMsg = CombatMessage.actionMessage(
      usePropsAction
    );

    var attackAtion= new TurnAction(TurnActionType.ATTACK);
    var attackMsg = CombatMessage.actionMessage(attackAtion);

    var connectReq = CombatMessage.conncetRequestMessage(5L, 1);

    var serverRequest = CombatMessage.serverRequest(
      new ServerRequest<UsePropsRequest>(new UsePropsRequest(1, 1, 1))
    );

    var serverMessage= CombatMessage.serverMessage("Combat Ok");

    PropUsedResult propUsedResult = new PropUsedResult(
      List.of(new PropertyChangeDto(PetPropertiesName.HP, 10))
    );
    ClientResponse clientRes = new ClientResponse(propUsedResult);
    CombatMessage clientResMsg = CombatMessage.clientResponse(clientRes);

    var objects = List.of(
      TurnActionType.ATTACK,
      // message,
      // connectReq,
      // usePropsAction,
      // userPropsMsg,
      // propUsedResult,
      // clientRes,
      // clientResMsg,
      // serverRequest,
      serverMessage,
      attackAtion,
      attackMsg 
    );

    objects.forEach(t -> {
      String json = showJson(t);
      if (json != null) {
        Object obj;
        try {
          obj = objectMapper.readValue(json, t.getClass());
          System.out.println("Deserialized: " + obj + "\n");
        } catch (JsonProcessingException e) {
          System.out.println("error:" + e.getMessage());
        }
      }
    });
  }

  private String showJson(Object object) {
    String json;
    try {
      json = objectMapper.writeValueAsString(object);
      System.out.println(String.format("Object: %s\nJson: %s", object, json));
      return json;
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
