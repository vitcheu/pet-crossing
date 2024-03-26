package com.vitcheu.owner.service;

import java.util.Optional;

import com.vitcheu.common.model.combat.messages.TurnAction;
import com.vitcheu.common.model.combat.messages.TurnResult;
import com.vitcheu.common.model.request.BuyPropRequest;
import com.vitcheu.common.model.request.PetRequest;
import com.vitcheu.common.model.response.PropUsedResult;

public interface GamePlayService {
  /**
   * use prop(s) for the given pet
 * @param userId 
 * @param petId
 * @param propId
 * @param count
   * @return
   */
  PropUsedResult useProp(long userId, int petId, int propId, int count);

  /**
   * buy prop(s)
   * @param req
   */
  void buyProp(BuyPropRequest req);

  /*
   * add given pet to the owner
   */
  void addPet(PetRequest petRequest);

  void removePet(Integer petId);

  void playWith(Integer petId);

  void sendMessage(String msg);

  void startBattle(int petId, long vs);

  boolean getConnectionState();

  void disconnect();

  void postTurnAction(TurnAction action);

  Optional<TurnResult> getTurnResult();
}
