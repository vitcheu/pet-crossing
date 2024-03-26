package com.vitcheu.owner.service;

import org.springframework.lang.NonNull;

import com.vitcheu.owner.model.event.*;

public interface GameEventListener {
  void onTurnResult(@NonNull TurnResultEvent e);

  void onCombatReadyEvent(@NonNull CombatReadyEvent e);
}
