package com.vitcheu.owner.web.client;

import java.net.URI;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.vitcheu.common.constants.api.PublicUrl;
import com.vitcheu.common.exception.ResourceNotFoundException;
import com.vitcheu.common.model.response.ProfileResponse;
import com.vitcheu.owner.config.CacheConfig;
import com.vitcheu.owner.constants.UrlContants;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProfileClient {

  @Resource
  private RestTemplate restTemplate;

  @Cacheable(cacheNames = CacheConfig.CACHE_REMOTE_PROFILE)
  public ProfileResponse getProfileFromRemote(Long userId) {
    String baseUrl = UrlContants.PROFILE_SERVICE + PublicUrl.USER_PROFILE;
    URI uri = UriComponentsBuilder
      .fromUriString(baseUrl)
      .pathSegment("{id}")
      .build(userId);
    final var res = restTemplate.exchange(
      uri,
      HttpMethod.GET,
      null,
      ProfileResponse.class
    );
    ProfileResponse profile = res.getBody();
    if (profile == null) {
      throw new ResourceNotFoundException(
        "profile of user@" + userId + " not found!"
      );
    }

    log.info(
      String.format(
        "retrive profiel of User@%s from remote: %s",
        userId,
        profile
      )
    );
    return profile;
  }
}
