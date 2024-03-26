package com.vitcheu.owner.model.combat;

import com.vitcheu.common.model.combat.messages.TurnResult;
import lombok.Data;

@Data
public class CombatInfo {

  enum CombatState {
    Connecting,
    Combating,
    Disconnected,
  }

  private CombatState state;

  private final long oponentId;

  private final int selectedPetId;

  private TurnResult lastTurnResult;

  public CombatInfo(long oponentId, int selectedPetId) {
    this.state = CombatState.Connecting;
    this.oponentId = oponentId;
    this.selectedPetId = selectedPetId;
  }

  public void startCombat() {
    this.state = CombatState.Combating;
  }

  public void endCombat() {
    this.state = CombatState.Disconnected;
  }

  public boolean isConnecting() {
    return this.state != CombatState.Disconnected;
  }
}
