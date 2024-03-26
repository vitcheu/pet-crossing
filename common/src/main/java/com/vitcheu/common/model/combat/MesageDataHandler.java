package com.vitcheu.common.model.combat;

import com.vitcheu.common.model.combat.messages.*;

public interface MesageDataHandler {
  default void handle(CombatData data) {}

  default void handle(ConnectRequest req) {}

  default void handle(TurnAction action) {}

  default void handle(ServerMessage message) {}

  default <T> void handle(ServerRequest<T> request) {}

  default void handle(TurnResult turnResult) {}
}
