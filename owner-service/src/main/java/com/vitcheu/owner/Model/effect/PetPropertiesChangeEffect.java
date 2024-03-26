package com.vitcheu.owner.model.effect;

import com.vitcheu.common.model.*;
import com.vitcheu.owner.model.event.PetPropertiesChangedEvent;
import com.vitcheu.owner.utils.AppEventPublisher;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class PetPropertiesChangeEffect implements Effect<PetDetails> {

  private final PetPropertiesName propertiesName;
  private final int changValue;

  @Override
  public <E extends PetDetails> void takesEffect(E targetEntity, int count) {
    /* change locally */
    PetProperties properties = targetEntity.getProperties();
    properties.changPropertyByValue(propertiesName, changValue*count);

    /* change remotely */
    AppEventPublisher.publishEvent(
      new PetPropertiesChangedEvent(
        targetEntity.getOwnerId(),
        true,
        new Propertychange(targetEntity.getId(), propertiesName, changValue*count)
      )
    );
  }

  public static Effect<PetDetails> hpchangEffect(int changValue) {
    return new PetPropertiesChangeEffect(PetPropertiesName.HP, changValue);
  }

  public static Effect<PetDetails> mpchangEffect(int changValue) {
    return new PetPropertiesChangeEffect(PetPropertiesName.MP, changValue);
  }
}
