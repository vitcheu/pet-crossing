package com.vitcheu.authentication.user.authorization;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.vitcheu.authentication.user.model.User;

public interface RBACuserService {
  void createUser(User user);

  Optional<User> getUserByUsername(String username);

  ResponseEntity<?> deleteUser(String username);

  List<User> getAllUsers();
}
