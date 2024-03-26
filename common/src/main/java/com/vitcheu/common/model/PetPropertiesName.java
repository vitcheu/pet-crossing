package com.vitcheu.common.model;

public enum PetPropertiesName {
  ATK("atk"),
  DEF("def"),
  HP("hp"),
  MP("mp"),
  SPEED("speed"),
  LUCK("luck"),
  FAVORABILITY("favorability");

  private final String declaredName;

  PetPropertiesName(String declaredName) {
    this.declaredName = declaredName;
  }

  public String declaredName() {
    return this.declaredName;
  }
}
