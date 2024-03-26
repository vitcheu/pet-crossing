package com.vitcheu.pet.service.impl;

import static com.vitcheu.pet.config.CacheConfig.*;

import com.vitcheu.common.exception.ResourceNotFoundException;
import com.vitcheu.common.model.request.PetRequest;
import com.vitcheu.pet.model.*;
import com.vitcheu.pet.repository.PetRepository;
import com.vitcheu.pet.service.PetService;
import jakarta.annotation.Resource;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetServiceImpl implements PetService {

  @Resource
  private PetRepository petRepository;

  @Override
  @Cacheable(cacheNames = CACHE_SINGLE_PET, key = "#p0")
  public Pet findPetById(int petId) {
    log.info("find pet by id:" + petId);
    return petRepository
      .findById(petId)
      .orElseThrow(() ->
        new ResourceNotFoundException("Pet " + petId + " not found")
      );
  }

  @Override
  public List<Pet> findPetsByIds(Integer... ids) {
    log.info("find pets by ids:" + Arrays.toString(ids));
    // List<Pet> pets = petRepository.findAllById(List.of(ids));
    List<Pet> pets = Arrays
      .stream(ids)
      .map(i -> currentProxy().findPetById(i))
      .toList();

    return pets;
  }

  @Override
  @Cacheable(cacheNames = CACHE_PET_TYPES)
  public List<PetType> getPetTypes() {
    log.info("getting pet types...");
    return petRepository.findPetTypes();
  }

  @Override
  public Pet createPet(PetRequest petRequest, Long ownerId) {
    log.info("creating pet:" + petRequest + " of owner@" + ownerId);
    final Pet pet = new Pet();
    pet.setOwnerId(ownerId);

    Pet saved = savePet(pet, petRequest);
    return saved;
  }

  @Override
  public void updatePet(PetRequest petRequest, int petId) {
    log.info("creating pet:" + petRequest + ",id:" + petId);
    Pet pet = currentProxy().findPetById(petId);
    savePet(pet, petRequest);
  }

  @Override
  @CacheEvict(cacheNames = CACHE_SINGLE_PET, key = "#p0")
  public void removePet(Integer petId) {
    log.info("removing pet@" + petId);
    petRepository.deleteById(petId);
  }

  @Override
  @Cacheable(cacheNames = CACHE_PET_TYPE)
  public Optional<PetType> findPetType(int typeId) {
    return petRepository.findPetTypeById(typeId);
  }

  @Override
  @CachePut(cacheNames = CACHE_SINGLE_PET, key = "#p0.id")
  public Pet save(Pet pet) {
    log.info("Saving pet {}", pet);
    return petRepository.save(pet);
  }

  private Pet savePet(Pet pet, PetRequest petRequest) {
    pet.setName(petRequest.name());
    pet.setBirthDate(petRequest.birthDate());

    /* sets default values to pet properties */
    pet.setProperties(PetPropertiesPo.defualtProperties());

    setPetType(pet, petRequest);

    return currentProxy().save(pet);
  }

  private PetServiceImpl currentProxy() {
    return (PetServiceImpl) AopContext.currentProxy();
  }

  private void setPetType(Pet pet, PetRequest petRequest) {
    currentProxy().findPetType(petRequest.typeId()).ifPresent(pet::setType);
  }
}
