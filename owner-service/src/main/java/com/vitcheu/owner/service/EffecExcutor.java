package com.vitcheu.owner.service;

import com.vitcheu.owner.model.dto.Prop;

public interface EffecExcutor {
  public <T> void excute(Prop prop, T targetEntity, int count);
}
