package com.vitcheu.pet.web;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.vitcheu.common.model.PetPropertiesName;
import com.vitcheu.common.model.Propertychange;
import com.vitcheu.common.model.response.ApiResponse;
import com.vitcheu.pet.model.Pet;
import com.vitcheu.pet.model.PetPropertiesPo;
import com.vitcheu.pet.repository.PetRepository;
import com.vitcheu.pet.service.ChangPropertiesService;
import com.vitcheu.pet.service.PetService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/properties")
@Slf4j
public class PetStatesAndPropertiesController {

  @Resource
  PetRepository petRepository;
  @Resource
  PetService petService;

  @Resource
  ChangPropertiesService changPropertiesService;

  @Transactional
  @PostMapping
  public ApiResponse<Object> processPropertiesChangedRequest(
    @RequestBody Propertychange req
  ) {
    Integer petId = req.petId();
    int value = req.value();
    final var propertiesName = req.propertiesName();

    log.info(
      String.format(
        "Pet with Id %s changs property %s by %d",
        petId,
        propertiesName,
        value
      )
    );

    doChange(petId, value, propertiesName);

    return ApiResponse.builder().message("Properties changed.").build();
  }

  
  private void doChange(Integer petId, int value, final PetPropertiesName propertiesName) {
    Pet pet = petService.findPetById(petId);
    PetPropertiesPo properties = pet.getProperties();
    changPropertiesService.changeProperties(properties, propertiesName, value);

    /* triggles cache and database update */
    petService.save(pet);
  }
}
