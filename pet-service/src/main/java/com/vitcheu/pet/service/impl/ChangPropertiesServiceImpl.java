
package com.vitcheu.pet.service.impl;

import java.lang.reflect.Field;

import org.springframework.stereotype.Service;

import com.vitcheu.common.model.PetPropertiesName;
import com.vitcheu.pet.model.PetPropertiesPo;
import com.vitcheu.pet.service.ChangPropertiesService;

@Service
public class ChangPropertiesServiceImpl implements ChangPropertiesService {
  @Override
public void changeProperties(
    PetPropertiesPo properties,
    PetPropertiesName petPropertiesName,
    int changValue
  ) {
    var pc = properties.getClass();
    try {
      Field field = pc.getDeclaredField(petPropertiesName.declaredName());
      field.setAccessible(true);

      Integer oriValue = (Integer) field.get(properties);
      var curValue = oriValue + changValue;
      field.set(properties, curValue);
    } catch (
      NoSuchFieldException
      | SecurityException
      | IllegalArgumentException
      | IllegalAccessException e
    ) {
      throw new RuntimeException(e);
    }
  }
}
