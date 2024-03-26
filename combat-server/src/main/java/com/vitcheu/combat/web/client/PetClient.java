package com.vitcheu.combat.web.client;

import java.net.URI;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.vitcheu.combat.contants.UrlConstants;
import com.vitcheu.common.exception.ResourceNotFoundException;
import com.vitcheu.common.model.PetDetails;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Component
@Profile("!dev")
@Slf4j
public class PetClient implements IPetClient {

  @Resource
  private RestTemplate restTemplate;

  @Override
  public PetDetails getPetFromRemote(Integer id) {
    log.info("Getting pet@" + id + " from remote....\n");
    String baseUrl = UrlConstants.PET_SERVICE + "/pets";
    URI uri = UriComponentsBuilder
      .fromUriString(baseUrl)
      .pathSegment("{id}")
      .build(id);
    final var res = restTemplate.exchange(
      uri,
      HttpMethod.GET,
      null,
      PetDetails.class
    );
    PetDetails pet = res.getBody();
    if (pet == null) {
      throw new ResourceNotFoundException("pet: " + id + " not found!");
    }

    pet.setId(id);
    log.info(String.format("retrive pet with id:%s from remote: %s", id, pet));
    return pet;
  }
}
