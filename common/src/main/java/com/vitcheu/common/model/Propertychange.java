package com.vitcheu.common.model;

public record Propertychange(
  Integer petId,
  PetPropertiesName propertiesName,
  int value
) {}
