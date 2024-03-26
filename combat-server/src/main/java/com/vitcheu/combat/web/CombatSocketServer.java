package com.vitcheu.combat.web;


import static com.vitcheu.combat.context.PlayerContextHoler.getContext;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.vitcheu.combat.conifg.MessageDecoder;
import com.vitcheu.combat.context.*;
import com.vitcheu.combat.event.MessageReceivedEvent;
import com.vitcheu.combat.model.CombatInfo;
import com.vitcheu.combat.service.MessageSender;
import com.vitcheu.combat.service.TurnHandler;
import com.vitcheu.combat.utils.AppEventPublisher;
import com.vitcheu.common.constants.api.PublicUrl;
import com.vitcheu.common.model.combat.MesageDataHandler;
import com.vitcheu.common.model.combat.messages.*;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

@Component
@ServerEndpoint(
  value = PublicUrl.COMBAT_SERVER + "/{userId}",
  decoders = { MessageDecoder.class }
)
@Slf4j
public class CombatSocketServer implements MesageDataHandler {

  private static PlayerContext getContextNonnull(long userId) {
    PlayerContext context = getContext(userId);
    Objects.requireNonNull(context, "Player Context must not be null!");
    return context;
  }

  private static CombatInfo getCombatInfo(long userId) {
    if (!PlayerContextHoler.contains(userId)) {
      return null;
    }
    var context = getContextNonnull(userId);
    return context.getCombatInfo();
  }

  private TurnHandler turnHandler;

  private long userId;

  private MessageSender messageSender;

  @OnOpen
  public void onOpen(Session session, @PathParam("userId") String userId) {
    this.userId = Long.parseLong(userId);

    this.turnHandler = AppContextHolder.getBean(TurnHandler.class);
    this.messageSender = AppContextHolder.getBean(MessageSender.class);

    PlayerContext context = PlayerContext
      .builder()
      .session(session)
      .playerId(this.userId)
      .build();
    PlayerContextHoler.put(this.userId, context);

    log.info("conneted to " + userId);
  }

  @OnClose
  public void onClose() {
    clearContext();
    log.info("Connection of Player@" + userId + " closed.");
  }

  @OnMessage
  public void onMessage(Session session, CombatMessage message) {
    log.info(
      String.format("\n-------> [%s] Received message: %s\n", userId, message)
    );
    PlayerContextHoler.setLocalContext(userId);

    // brocast
    var event = new MessageReceivedEvent(this.userId, message);
    AppEventPublisher.publishEvent(event);

    handlleMessage(message);
    log.info(
      String.format(
        "\n-------> [%s] Done Handling message:%s\n",
        userId,
        message
      )
    );
  }

  @OnError
  public void onError(Session session, Throwable error) {
    log.error("error:" + error.getMessage());
  }

  @Override
  public void handle(ConnectRequest req) {
    var info = getCombatInfo(userId);
    if (info != null) {
      String msg = "Already connected. Do Not Send Again!";
      messageSender.sendText(userId, msg);
      return;
    }

    long oponent = req.getTo();
    var oponentInfo = getCombatInfo(oponent);

    if (oponentInfo == null) {
      log.info(
        String.format(
          "connection between Player@%s and Player@%s has been established.",
          this.userId,
          oponent
        )
      );
      log.info("Waiting for Player@{} to connect", oponent);

      info = new CombatInfo(this.userId, req.getPetId());
      fillContext(info, req);
    } else {
      /* both connections have been established */
      info = oponentInfo;
      log.info("Exsiting Combat Info: " + info);

      fillContext(info, req);
      info.addOpenentInfo(this.userId, req.getPetId());

      /*   inform both sides players that the combat is ready to start. */
      String readyMsg = "Combat Ok.\nCombatId:" + info.getCombatId();
      messageSender.sendText(userId, readyMsg);
      messageSender.sendText(oponent, readyMsg);

      info.ready();
    }
  }

  @Override
  public void handle(TurnAction action) {
    TurnAction preAction = getTurnAction();
    if (preAction != null) {
      String msg = "Current turn action was Already sent. Do Not Send Again!";
      messageSender.sendText(userId, msg);
      return;
    }
    setTurnAction(action);

    var oponentAction = getOponentAction();
    if (oponentAction == null) {
      String msg = String.format(
        "Waiting for Player@%d to choose action.",
        getOponentId()
      );
      messageSender.sendText(userId, msg);
    } else {
      var combatInfo = getCombatInfo(userId);

      assert !combatInfo.isHandling();
      combatInfo.handling();
      turnHandler.handle(
        combatInfo,
        userId,
        (
          result -> {
            onCompleteHandle(combatInfo, result);
          }
        )
      );
    }
  }

  private void clearContext() {
    PlayerContextHoler.remove(this.userId);
  }

  private void onCompleteHandle(CombatInfo combatInfo, TurnResult result) {
    log.info("Received Result:" + result);
    /* send result */
    var resultMsg = CombatMessage.turnResult(result);
    messageSender.sendMessage(userId, resultMsg);
    messageSender.sendMessage(getOponentId(), resultMsg);

    combatInfo.ready();
    clearActions();
  }

  private void handlleMessage(CombatMessage message) {
    var payload = message.getPayload();
    payload.accept(this);
  }

  private void clearActions() {
    CombatInfo combatInfo = getCombatInfo(userId);
    combatInfo.clearActions();
  }

  private void fillContext(CombatInfo info, ConnectRequest req) {
    PlayerContext context = getContextNonnull(this.userId);
    context.setOponentId(req.getTo());
    context.setSelectedPetId(req.getPetId());
    context.setCombatInfo(info);
  }

  private void setTurnAction(TurnAction action) {
    CombatInfo combatInfo = getCombatInfo(userId);
    combatInfo.setTurnAction(userId, action);
  }

  private TurnAction getTurnAction() {
    CombatInfo combatInfo = getCombatInfo(userId);
    return combatInfo.getTurnAction(userId);
  }

  private long getOponentId() {
    PlayerContext context = getContextNonnull(userId);
    return context.getOponentId();
  }

  private TurnAction getOponentAction() {
    var oponentId = getOponentId();
    CombatInfo combatInfo = getCombatInfo(userId);
    return combatInfo.getTurnAction(oponentId);
  }
}
