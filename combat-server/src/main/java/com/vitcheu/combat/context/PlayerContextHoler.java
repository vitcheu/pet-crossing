package com.vitcheu.combat.context;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerContextHoler {

  private static final ConcurrentHashMap<Long, PlayerContext> contextMap = new ConcurrentHashMap<>();
  private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

  public static PlayerContext getContext(long userId) {
    return contextMap.get(userId);
  }

  public static PlayerContext getContext() {
    return contextMap.get(getUserId());
  }

  public static void put(long userId, PlayerContext context) {
    contextMap.put(userId, context);
  }

  public static void remove(long userId) {
    contextMap.remove(userId);
  }

  public static boolean contains(long userId) {
    return contextMap.containsKey(userId);
  }

  public static void setLocalContext(long userId) {
    userIdHolder.set(userId);
  }

  public static Long getUserId() {
    return userIdHolder.get();
  }
}
