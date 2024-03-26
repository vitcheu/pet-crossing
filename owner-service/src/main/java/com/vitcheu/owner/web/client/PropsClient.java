package com.vitcheu.owner.web.client;

import com.vitcheu.common.constants.api.PublicUrl;
import com.vitcheu.common.exception.RemoteAccessException;
import com.vitcheu.common.model.request.BuyPropRequest;
import com.vitcheu.common.model.response.ApiResponse;
import com.vitcheu.owner.config.CacheConfig;
import com.vitcheu.owner.constants.UrlContants;
import com.vitcheu.owner.model.dto.Prop;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class PropsClient {

  @Resource
  RestTemplate restTemplate;

  @Cacheable(cacheNames = CacheConfig.SINGLE_PROP)
  public Prop getPropFromRemote(Integer id) {
    String url = UriComponentsBuilder
      .fromUriString(UrlContants.PROP_SERVICE + PublicUrl.GET_PROPS)
      .pathSegment("{id}")
      .build(String.valueOf(id))
      .toString();
    var prop = restTemplate.getForObject(url, Prop.class);
    log.info(

      String.format("retrive prop with id:+%d from remote: %s", id, prop)
    );
    if (prop == null) {
      return null;
    }
    prop.setId(id);

    return prop;
  }

  public void buyPropRequest(BuyPropRequest req) {
    String url = UrlContants.PROP_SERVICE + PublicUrl.PROP_BUY;
    try {
      var response = restTemplate.postForEntity(url, req, ApiResponse.class);

      log.info("response of post buy request :" + response.getBody());
    } catch (Exception e) {
      log.error("buy request failed, caused by : " + e.getMessage(), e);
      throw new RemoteAccessException(e.getMessage());
    }
  }
}
