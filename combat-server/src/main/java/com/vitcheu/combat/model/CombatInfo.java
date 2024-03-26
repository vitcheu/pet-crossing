package com.vitcheu.combat.model;

import java.util.*;

import com.vitcheu.common.model.combat.messages.TurnAction;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * server side combat related data.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class CombatInfo {

  @Getter
  @ToString
  public static class PlayerInfo {

    long playerId;
    int petId;

    volatile TurnAction action;

    public PlayerInfo(long playerId, int petId) {
      this.playerId = playerId;
      this.petId = petId;
    }

    public void setAction(TurnAction action) {
      this.action = action;
    }
  }

  static enum CombatState {
    CONNECTING,

    COMBATTING,

    DISCONNECTING,

    DISCONNECTED,

    HANDLING,
  }

  @Getter
  @ToString.Exclude
  String combatId;

  volatile Map<Long, PlayerInfo> playerInfos = new HashMap<>();

  volatile CombatState state;

  public CombatInfo(long playerA, int petId) {
    this.combatId = UUID.randomUUID().toString();

    PlayerInfo info1 = new PlayerInfo(playerA, petId);
    playerInfos.put(playerA, info1);
  }

  public void addOpenentInfo(long playerB, int petId) {
    PlayerInfo info2 = new PlayerInfo(playerB, petId);
    playerInfos.put(playerB, info2);
  }

  public void setTurnAction(long playerId, TurnAction action) {
    PlayerInfo playerInfo = playerInfos.get(playerId);
    Objects.requireNonNull(playerInfo, "No playerInfo present!");

    playerInfo.setAction(action);
  }

  public TurnAction getTurnAction(long playerId) {
    PlayerInfo playerInfo = playerInfos.get(playerId);
    Objects.requireNonNull(playerInfo, "No playerInfo present!");
    return playerInfo.getAction();
  }

  public synchronized void ready() {
    this.state = CombatState.COMBATTING;
  }

  public synchronized void handling() {
    this.state = CombatState.HANDLING;
  }

  public synchronized boolean isHandling() {
    return this.state.equals(CombatState.HANDLING);
  }

  public PlayerInfo getFirstPlayer() {
    return getPlayerInfos().get(0);
  }

  public PlayerInfo getSecondPlayer() {
    return getPlayerInfos().get(1);
  }

  public void clearActions() {
    playerInfos.values().forEach((PlayerInfo t) -> t.setAction(null));
  }

  private List<PlayerInfo> getPlayerInfos() {
    Collection<PlayerInfo> values = playerInfos.values();
    return new ArrayList<>(values);
  }
}
