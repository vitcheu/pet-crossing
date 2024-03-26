package com.vitcheu.combat.service;

import java.util.function.Consumer;

import com.vitcheu.combat.event.MessageReceivedEvent;
import com.vitcheu.combat.model.CombatInfo;
import com.vitcheu.common.model.combat.TurnActionTypeHandler;
import com.vitcheu.common.model.combat.messages.TurnResult;

public interface TurnHandler extends TurnActionTypeHandler {
  void handle(CombatInfo combatInfo, long userId,Consumer<TurnResult> resultHandler);
  void onMessageReceived(MessageReceivedEvent event);
}
