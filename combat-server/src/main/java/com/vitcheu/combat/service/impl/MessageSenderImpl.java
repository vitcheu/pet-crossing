
package com.vitcheu.combat.service.impl;

import static com.vitcheu.combat.context.PlayerContextHoler.getContext;

import java.io.IOException;

import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.vitcheu.combat.event.SendSocketMessageRequestEvent;
import com.vitcheu.combat.service.MessageSender;
import com.vitcheu.common.model.combat.messages.CombatMessage;
import com.vitcheu.common.utils.JsonUtils;

import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageSenderImpl implements MessageSender {
  @Override
  public  void sendMessage(long userId, CombatMessage message) {
    var playerContext = getContext(userId);
    var session = playerContext.getSession();
    if (session == null) {
      log.info(userId + " is not connected");
    } else {
      log.info("sending :" + message + "\nto User@" + userId);
      sendMessage(session, message);
    }
  }


  @Override
  @EventListener
  public void OnSendMessageEvent(@NonNull SendSocketMessageRequestEvent event) {
    log.info("Received event: " + event);
    var message = event.getMessage();
    var userId = event.getUserId();

    // PlayerContextHoler.getUserId();
    sendMessage(userId, message);
  }

  private  void sendMessage(Session session, CombatMessage msgToSend) {
    try {
      String json = JsonUtils.toJsonStr(msgToSend);
      session.getBasicRemote().sendText(json);
    } catch (IOException e) {
      log.warn("send message failed: " + msgToSend);
      e.printStackTrace();
    }
  }


  @Override
  public void sendText(long userId, String text) {
    var msg=CombatMessage.serverMessage(text);
    sendMessage(userId, msg);
  }
}
