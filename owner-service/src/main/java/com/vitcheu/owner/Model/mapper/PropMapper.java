package com.vitcheu.owner.model.mapper;

import com.vitcheu.owner.model.dto.Prop;
import com.vitcheu.owner.model.dto.PropDetails;
import com.vitcheu.owner.model.po.OwnedProp;

public class PropMapper {

  public static PropDetails mapToDetails(Prop prop, OwnedProp ownedProp) {
    PropDetails propDetails = PropDetails
      .builder()
      .name(prop.getName())
      .description(prop.getDescription())
      .type(prop.getType())
      .amount(ownedProp.getAmount())
      .build();

    return propDetails;
  }
}
