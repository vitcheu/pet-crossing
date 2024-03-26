package com.vitcheu.combat.model.builder;

import java.util.*;

import com.vitcheu.common.model.PetPropertiesName;
import com.vitcheu.common.model.combat.PropertyChangeDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertychangDtoBuilder {

  Map<PetPropertiesName,PropertyChangeDto> propertychangDtos;

  public PropertychangDtoBuilder() {
    this.propertychangDtos = new HashMap<>();
  }

  public PropertychangDtoBuilder chaneBy(
    PropertyChangeDto dto
  ) {
    log.debug("changby: "+dto);
    // throw new RuntimeException();

    final var dto1 = propertychangDtos.get(dto.getPropertiesName()) ;
    if(dto1==null){
       propertychangDtos.put(dto.getPropertiesName(), dto);
    }else{
      addChange(dto,dto1);
    }

    return this;
  }

  private void addChange(PropertyChangeDto dto, PropertyChangeDto dto1) {
    assert dto.getPropertiesName().equals(dto1.getPropertiesName());
    int v = dto.getValue();
    int v1 = dto1.getValue(); 
    dto.setValue(v+v1);
  }

  public List<PropertyChangeDto> build(){
    return new ArrayList<>(propertychangDtos.values());
  }
}
