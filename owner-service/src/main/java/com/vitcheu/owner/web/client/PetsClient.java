package com.vitcheu.owner.web.client;

import com.vitcheu.common.constants.api.PublicUrl;
import com.vitcheu.common.exception.OtherExceptions;
import com.vitcheu.common.exception.ResourceNotFoundException;
import com.vitcheu.common.model.PetDetails;
import com.vitcheu.common.model.Propertychange;
import com.vitcheu.common.model.request.PetRequest;
import com.vitcheu.common.model.response.ApiResponse;
import com.vitcheu.owner.config.CacheConfig;
import com.vitcheu.owner.constants.UrlContants;
import com.vitcheu.owner.context.UserContext;
import com.vitcheu.owner.model.event.PetPropertiesChangedEvent;
import jakarta.annotation.Resource;
import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.*;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class PetsClient
  implements ApplicationListener<PetPropertiesChangedEvent> {

  @Resource
  private RestTemplate restTemplate;

  public List<PetDetails> getPetsFromRemote(List<Integer> ids) {
    var list = ids
      .stream()
      .map(i -> currentProxy().getPetFromRemote(i))
      .toList();
    return list;
  }

  @Cacheable(cacheNames = CacheConfig.CACHE_REMOTE_SINGLE_PET)
  public PetDetails getPetFromRemote(Integer id) {
    log.info("Getting pet@" + id + " from remote....\n");
    String baseUrl = UrlContants.PET_SERVICE + "/pets";
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

  @CacheEvict(
    cacheNames = CacheConfig.CACHE_REMOTE_SINGLE_PET,
    key = "#p0.petId"
  )
  public void postChangPropertiesrequest(Propertychange req) {
    String url = UrlContants.PET_SERVICE + PublicUrl.PET_PROPERTIES;
    try {
      var response = restTemplate.postForEntity(url, req, ApiResponse.class);
      log.info("response of the posted request :" + response.getBody());
    } catch (Exception e) {
      e.printStackTrace();
      log.error("post request failed, caused by : " + e.getMessage(), e);
    }
  }

  @CachePut(cacheNames = CacheConfig.CACHE_REMOTE_SINGLE_PET, key = "#p0.id")
  public PetDetails postAddingPetRequest(PetRequest petRequest) {
    long userId = UserContext.getUserId();
    URI uri = UriComponentsBuilder
      .fromUriString(UrlContants.PET_SERVICE + "owners")
      .pathSegment("{ownerId}", "pets")
      .build(String.valueOf(userId));

    ResponseEntity<PetDetails> postForEntity = restTemplate.postForEntity(
      uri,
      petRequest,
      PetDetails.class
    );
    PetDetails petDetails = postForEntity.getBody();
    log.info("petDetails: " + petDetails);

    return petDetails;
  }

  @CacheEvict(cacheNames = CacheConfig.CACHE_REMOTE_SINGLE_PET, key = "#p0")
  public void removePet(Integer petId) {
    URI uri = UriComponentsBuilder
      .fromUriString(UrlContants.PET_SERVICE + "/pets")
      .pathSegment("{petId}")
      .build(String.valueOf(petId));

    try {
      restTemplate.delete(uri);
    } catch (Exception e) {
      e.printStackTrace();
      throw new OtherExceptions("deleted Pet@" + petId + " failed.");
    }
  }

  private PetsClient currentProxy() {
    return (PetsClient) AopContext.currentProxy();
  }

  @Override
  public void onApplicationEvent(@NonNull PetPropertiesChangedEvent event) {
    List<Propertychange> changes = event.getChanges();
    log.info("handling properties changes:" + changes);
    changes.forEach(c -> postChangPropertiesrequest(c));
  }
}
