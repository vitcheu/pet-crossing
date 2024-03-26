package com.vitcheu.authentication.web;

import java.util.Optional;

import org.springframework.web.bind.annotation.*;

import com.vitcheu.authentication.user.model.User;
import com.vitcheu.authentication.user.repository.UserRepository;
import com.vitcheu.common.constants.api.PublicUrl;
import com.vitcheu.common.exception.ResourceNotFoundException;
import com.vitcheu.common.model.response.ProfileResponse;

import jakarta.annotation.Resource;

@RestController
@RequestMapping(PublicUrl.USER_PROFILE)
public class ProfileResource {

  @Resource
  UserRepository userRepository;

  @GetMapping("/{id}")
  public ProfileResponse getProfile(@PathVariable("id") Long id) {
    Optional<User> byId = userRepository.findById(id);

    if (byId.isPresent()) {
      User user = byId.get();
      ProfileResponse response = new ProfileResponse(
        user.getUsername(),
        user.getEmail()
      );
      return response;
    } else {
      throw new ResourceNotFoundException("User@" + id + " does not exist.");
    }
  }
}
