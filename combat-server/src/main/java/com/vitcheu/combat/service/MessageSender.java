package com.vitcheu.combat.service;

import org.springframework.lang.NonNull;

import com.vitcheu.combat.event.SendSocketMessageRequestEvent;
import com.vitcheu.common.model.combat.messages.CombatMessage;

public interface MessageSender {
  void OnSendMessageEvent(@NonNull SendSocketMessageRequestEvent event);

  void sendMessage(long userId, CombatMessage message);

  void sendText(long userId,String text);
}
