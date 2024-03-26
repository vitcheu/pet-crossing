package com.vitcheu.prop.web;

import com.vitcheu.common.constants.api.PublicUrl;
import com.vitcheu.common.exception.ResourceNotFoundException;
import com.vitcheu.common.model.request.BuyPropRequest;
import com.vitcheu.common.model.response.ApiResponse;
import com.vitcheu.prop.config.Constants;
import com.vitcheu.prop.context.UserContext;
import com.vitcheu.prop.model.Prop;
import com.vitcheu.prop.repository.PropRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
class PropResource {

  private final PropRepository propRepository;

  @GetMapping(PublicUrl.GET_PROPS)
  @Cacheable(cacheNames = Constants.CACHE_PROPLIST)
  public List<Prop> showPropList() {
    return propRepository.findAll();
  }

  @Cacheable(cacheNames = Constants.CACHE_PROP)
  @GetMapping(PublicUrl.GET_PROPS + "/{id}")
  public Prop getProp(@PathVariable(name = "id") Integer id) {
    log.info("Get Prop:" + id);
    Optional<Prop> byId = propRepository.findById(id);
    if (byId.isPresent()) {
      log.info("return " + byId.get());
      return byId.get();
    } else {
      log.info("Prop#"+id+" not found!");
      throw new ResourceNotFoundException("Prop#"+id+" not found!");
    }
  }

  @PostMapping(value = PublicUrl.PROP_BUY)
  public ApiResponse<Object> buyProp(@RequestBody BuyPropRequest req) {
    Long userId = UserContext.getUserId();
    Integer propId = req.propId();
    int quantity = req.quantity();
    log.info(
      String.format("User@%s is buying %s Prop@%s(s)", userId, propId, quantity)
    );

    propRepository.updateAmount(propId, quantity);

    return ApiResponse.builder().message("Bought succeeded!").build();
  }
}
