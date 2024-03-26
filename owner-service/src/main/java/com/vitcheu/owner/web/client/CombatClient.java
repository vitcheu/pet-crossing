package com.vitcheu.owner.web.client;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.vitcheu.common.model.combat.*;
import com.vitcheu.common.model.combat.messages.*;
import com.vitcheu.common.model.request.UsePropsRequest;
import com.vitcheu.common.utils.JsonUtils;
import com.vitcheu.owner.context.AppContextHolder;
import com.vitcheu.owner.context.UserContext;
import com.vitcheu.owner.model.event.CombatReadyEvent;
import com.vitcheu.owner.model.event.TurnResultEvent;
import com.vitcheu.owner.service.GamePlayService;
import com.vitcheu.owner.utils.AppEventPublisher;

import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CombatClient
  extends WebSocketClient
  implements MesageDataHandler, AsyncServerMessageHandler {

  @Getter
  private final long userId;

  @Resource
  GamePlayService gamePlayService;

  public CombatClient(URI serverUri, long userId) {
    super(serverUri);
    this.userId = userId;
    this.gamePlayService = AppContextHolder.getBean(GamePlayService.class);
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
    log.info("client, opening, handshake:" + handshakedata);
  }

  @Override
  public void onMessage(String message) {
    var combatMessage = JsonUtils.toObject(message, CombatMessage.class);
    var payload = combatMessage.getPayload();

    handleData(payload);
  }

  @Override
  public void handleData(CombatData payload) {
    /* set context for handling */
    UserContext.setUserId(String.valueOf(userId));

    payload.accept(this);
  }

  @Override
  public void handle(TurnResult turnResult) {
    AppEventPublisher.publishEvent(new TurnResultEvent(userId, turnResult));
  }

  @Override
  public void handle(ServerMessage message) {
    log.info("Received Message:" + message);
    var text = message.getText();

    if (text.contains("Combat Ok")) {
      log.info("Ready to combat.");
      AppEventPublisher.publishEvent(new CombatReadyEvent(this.userId));
    }
  }

  @Override
  public <T> void handle(ServerRequest<T> request) {
    log.info("handling request from server:" + request);
    var body = request.getRequestBody();
    if (body instanceof UsePropsRequest req) {
      useProps(req);
    }
  }

  private void useProps(UsePropsRequest request) {
    var propUsedResult = gamePlayService.useProp(
      userId,
      request.petId(),
      request.propId(), request.count()
    );
    // var propUsedResult = new PropUsedResult(
    //   List.of(new PropertyChangeDto(PetPropertiesName.HP, 10))
    // );
    var clientRes = new ClientResponse(propUsedResult);
    var clientResMsg = CombatMessage.clientResponse(clientRes);

    sendMessage(clientResMsg);
  }

  public void sendMessage(CombatMessage message) {
    String jsonStr = JsonUtils.toJsonStr(message);
    log.info("Sending: " + jsonStr);
    send(jsonStr);
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    log.info("Connection of User@" + userId + " is closing, code=" + code);
  }

  @Override
  public void onError(Exception ex) {
    log.warn("error occured: " + ex.getMessage());
    ex.printStackTrace();
  }
}
