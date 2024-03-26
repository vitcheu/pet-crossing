package com.vitcheu.owner.service;

import com.vitcheu.common.model.Propertychange;
import com.vitcheu.owner.model.event.PetPropertiesChangedEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EffectResultCollector {

  private ConcurrentHashMap<Long, List<Propertychange>> propEffect = new ConcurrentHashMap<>();

  @EventListener
  public void onPetPropertiesChangedEvent(
    @NonNull PetPropertiesChangedEvent e
  ) {
    if (e.isFromProps()) {
      long userId = e.getUserId();
      log.info(String.format("[Collector@%s] User@%s's Pets changes", this,userId, e.getChanges()));
      collect(userId, e.getChanges());
    }
  }

  /*
   * Get recent changes of a pet of the current user and clear the changes
   */
  public List<Propertychange> getRecentChanges(long userId) {
    /* defence copy */
    var res= propEffect.compute(
      userId,
      (Long k, List<Propertychange> v) -> {
        if (v == null) {
          return new ArrayList<>();
        } else {
          return new ArrayList<>(v);
        }
      }
    );
    propEffect.remove(userId);
    return res;
  }

  private void collect(long userId, List<Propertychange> changed) {
    propEffect.compute(
      userId,
      (Long k, List<Propertychange> v) -> {
        if (v == null) {
          return new ArrayList<>(changed);
        } else {
          v.addAll(changed);
          return v;
        }
      }
    );
  }
}
