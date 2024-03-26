package com.vitcheu.authentication.user.authorization;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vitcheu.authentication.user.model.User;
import com.vitcheu.authentication.user.repository.UserRepository;
import com.vitcheu.common.exception.ResourceNotFoundException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class RBACuserServiceImpl implements RBACuserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public void createUser(User user) {
    RBACuserServiceImpl.log.info("-----> createUser service");

    this.userRepository.save(user);
  }

  @Override
  public Optional<User> getUserByUsername(String username) {
    RBACuserServiceImpl.log.info("-----> getUserByUsername service");

    return this.userRepository.findByUsername(username);
  }

  @Override
  public ResponseEntity<?> deleteUser(String username) {
    RBACuserServiceImpl.log.info("-----> deletePerson service");

    User user =
      this.userRepository.findByUsername(username)
        .orElseThrow(() ->
          new ResourceNotFoundException("User", "username", username)
        );

    this.userRepository.delete(user);

    return ResponseEntity.ok().build();
  }

  @Override
  public List<User> getAllUsers() {
    return this.userRepository.findAll();
  }
}
