package com.vitcheu.common.model.combat;

import com.vitcheu.common.model.PetDetails;
import com.vitcheu.common.model.combat.messages.TurnAction;

/**
 * both sides turn acitons information
 * passed between internal methods.
 */
public record ActionInfo(
  PetDetails pet1,
  PetDetails pet2,
  TurnAction action1,
  TurnAction action2
) {}
