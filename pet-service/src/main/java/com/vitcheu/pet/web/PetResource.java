package com.vitcheu.pet.web;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.vitcheu.common.model.PetDetails;
import com.vitcheu.common.model.Pets;
import com.vitcheu.common.model.request.PetRequest;
import com.vitcheu.common.utils.Converter;
import com.vitcheu.pet.model.Pet;
import com.vitcheu.pet.model.PetType;
import com.vitcheu.pet.repository.PetRepository;
import com.vitcheu.pet.service.PetService;

import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Timed("petclinic.pet")
@RequiredArgsConstructor
@Slf4j
class PetResource {

  private final PetRepository petRepository;
  private final PetService petService;
  private final CacheManager cacheManager;

  @GetMapping("/petTypes")
  public List<PetType> getPetTypes() {
    return petService.getPetTypes();
  }

  @PostMapping("owners/{ownerId}/pets")
  @ResponseStatus(HttpStatus.CREATED)
  public PetDetails processCreationForm(
    @RequestBody PetRequest petRequest,
    @PathVariable("ownerId") @Valid @Min(1) Long ownerId
  ) {
    log.info("owner:" + ownerId + " creates pet:" + petRequest);
    Pet created = petService.createPet(petRequest, ownerId);
    return Converter.convert(created, PetDetails.class);
  }

  @PutMapping("/pets/{petId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void processUpdateForm(@RequestBody PetRequest petRequest) {
    int petId = petRequest.id();
    petService.updatePet(petRequest, petId);
  }

  @DeleteMapping("/pets/{petId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void processRemove(@PathVariable("petId") Integer petId) {
    log.info("Deleting Pet@" + petId);
    petService.removePet(petId);
  }

  @GetMapping("pets/{petId}")
  public PetDetails findPet(@PathVariable("petId") int petId) {
    Pet pet = petService.findPetById(petId);
    return Converter.convert(pet, PetDetails.class);
  }

  @GetMapping("pets")
  public Pets findPets(@RequestParam("ids") Integer... ids) {
    var pets = petService.findPetsByIds(ids);
    log.info("Retived pets: " + pets);

    var petDetails = pets
      .stream()
      .map(p -> Converter.convert(p, PetDetails.class))
      .toList();
    return new Pets(petDetails);
  }

  @GetMapping("/caches")
  public Map<String, Object> getCachesValues() {
    Map<String, Object> map = new HashMap<>();
    cacheManager
      .getCacheNames()
      .forEach(cacheName -> {
        var cache = cacheManager.getCache(cacheName);
        ConcurrentMap<?, ?> concurrentMap = (ConcurrentMap<?, ?>) cache.getNativeCache();
        concurrentMap.forEach((key, value) -> {
          System.out.println(
            "Cache Name: " + cacheName + ", Key: " + key + ", Value: " + value
          );
        });

        map.put(cacheName, concurrentMap);
      });
    return map;
  }
}
