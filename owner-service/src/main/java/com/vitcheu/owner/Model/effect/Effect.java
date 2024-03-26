package com.vitcheu.owner.model.effect;

public interface Effect<T> {
  <E extends T> void takesEffect(E targetEntity, int count);
}
