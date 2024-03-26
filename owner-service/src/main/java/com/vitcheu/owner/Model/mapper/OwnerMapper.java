package com.vitcheu.owner.model.mapper;

import com.vitcheu.owner.model.dto.OwnerDetails;
import com.vitcheu.owner.model.dto.PropDetails;
import com.vitcheu.owner.model.po.Owner;
import java.util.List;

public class OwnerMapper {

  public static OwnerDetails mapOwnerDetails(
    Owner owner,
    List<PropDetails> propDetails
  ) {
    var ownerDetails = OwnerDetails
      .builder()
      .userId(owner.getUserId())
      .money(owner.getMoney()) 
      .props(propDetails)
      .build();
    return ownerDetails;
  }
}
