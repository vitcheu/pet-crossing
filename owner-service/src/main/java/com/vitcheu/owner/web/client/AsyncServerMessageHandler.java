
package com.vitcheu.owner.web.client;

import org.springframework.scheduling.annotation.Async;

import com.vitcheu.common.model.combat.messages.CombatData;

public interface AsyncServerMessageHandler {
  @Async("taskExecutor")
  public void handleData(CombatData payload) ;
}
