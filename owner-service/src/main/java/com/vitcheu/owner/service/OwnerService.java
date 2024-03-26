package com.vitcheu.owner.service;

import com.vitcheu.common.model.PetDetails;
import com.vitcheu.common.model.request.AddUserRequest;
import com.vitcheu.owner.model.dto.OwnerDetails;
import com.vitcheu.owner.model.po.Owner;
import java.util.List;

public interface OwnerService {
  Owner getOwnerById(long ownerId);
  List<OwnerDetails> findAll();

  OwnerDetails findOne(long id);

  List<PetDetails> getAllPets(long ownerId);

  boolean addUser(AddUserRequest request);
}
