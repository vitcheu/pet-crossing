package com.vitcheu.authentication.web.client.impl;

import com.vitcheu.authentication.web.client.OwnerClient;
import com.vitcheu.common.constants.api.PublicUrl;
import com.vitcheu.common.model.request.AddUserRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class OwnerClientImpl implements OwnerClient {

  @Resource
  private RestTemplate restTemplate;

  @Override
  public boolean addUser(AddUserRequest request) {
    try {
      String url = "http://owner-service/" + PublicUrl.ADD_USER;
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      var requestEntity = new HttpEntity<>(request, headers);

      var entity = restTemplate.postForEntity(
        url,
        requestEntity,
        Boolean.class
      );

      Boolean result = entity.getBody();
      return result != null && result;
    } catch (Exception e) {
      log.error("Error while adding user: " + e.getMessage());
      return false;
    }
  }
}
