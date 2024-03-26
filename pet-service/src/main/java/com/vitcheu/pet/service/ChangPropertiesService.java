package com.vitcheu.pet.service;

import com.vitcheu.common.model.PetPropertiesName;
import com.vitcheu.pet.model.PetPropertiesPo;

public interface ChangPropertiesService {
  public void changeProperties(
    PetPropertiesPo properties,
    PetPropertiesName petPropertiesName,
    int changValue
  );
}
