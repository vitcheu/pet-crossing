package com.vitcheu.combat.context;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.vitcheu.combat.model.CombatInfo;

import jakarta.websocket.Session;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerContext {

  private static final int QUEUE_CAPACITY = 1;

  Session session;

  Long playerId;

  Long oponentId;

  Integer selectedPetId;

  CombatInfo combatInfo;

  BlockingQueue<Object> blockingQueue;

  public void push(Object obj){
    if (blockingQueue == null) {
      blockingQueue=new ArrayBlockingQueue<>(QUEUE_CAPACITY);
    }
    blockingQueue.add(obj); 
  }
}


