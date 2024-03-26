package com.vitcheu.pet.service;

import com.vitcheu.common.model.request.PetRequest;
import com.vitcheu.pet.model.Pet;
import com.vitcheu.pet.model.PetType;
import java.util.List;
import java.util.Optional;

public interface PetService {
  Pet findPetById(int petId);

  List<Pet> findPetsByIds(Integer... ids);

  List<PetType> getPetTypes();

  Pet createPet(PetRequest petRequest, Long ownerId);

  void updatePet(PetRequest petRequest, int petId);

  void removePet(Integer petId);
  Optional<PetType> findPetType(int typeId);

  Pet save(Pet pet);
}
