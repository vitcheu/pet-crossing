package com.vitcheu.combat.conifg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitcheu.common.model.combat.messages.CombatMessage;

import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;

public class MessageDecoder implements Decoder.Text<CombatMessage> {

  @Override
  public CombatMessage decode(String s) throws DecodeException {
    try {
      return new ObjectMapper().readValue(s, CombatMessage.class);
    } catch (JsonProcessingException e) {
      throw new DecodeException(s, e.getMessage());
    }
  }

  @Override
  public boolean willDecode(String s) {
    return true;
  }
}
