package com.vitcheu.common.model.combat;

import com.vitcheu.common.model.PetPropertiesName;
import com.vitcheu.common.model.Propertychange;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
public class PropertyChangeDto {

  @Setter(value = AccessLevel.PRIVATE)
  PetPropertiesName propertiesName;

  @Setter
  int value;

  public PropertyChangeDto(Propertychange c) {
    this.propertiesName=c.propertiesName();
    this.value=c.value();
  }
}
