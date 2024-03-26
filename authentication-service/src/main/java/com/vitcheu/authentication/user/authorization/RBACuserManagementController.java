package com.vitcheu.authentication.user.authorization;

import jakarta.validation.Valid;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vitcheu.authentication.user.model.*;

@Log4j2
@RestController
@RequestMapping("/rbac")
public class RBACuserManagementController {

  @Autowired
  private RBACuserServiceImpl userServiceImpl;

  @PostMapping("/user")
  public void createUser(@Valid @RequestBody User user) {
    List<Role> roles = user.getRoles();

    RBACuserManagementController.log.info(
      "-----> User Name : " + user.getUsername()
    );
    RBACuserManagementController.log.info(
      "-----> Roles Count : " + roles.size()
    );

    for (int i = 0; i < roles.size(); i++) {
      RBACuserManagementController.log.info(
        "-----> Role Name : " + roles.get(i).getName()
      );

      if (roles.get(i).getPermissions().isEmpty()) {
        RBACuserManagementController.log.info(
          "-----> Role doesn't have permissions explicitly defined : "
        );

        String resource = roles.get(i).getName().replace("ROLE_", "");

        RBACuserManagementController.log.info("-----> Resource : " + resource);

        ArrayList<Permission> permission = new ArrayList<>();

        permission.add(new Permission(resource + "_CREATE"));
        permission.add(new Permission(resource + "_READ"));
        permission.add(new Permission(resource + "_UPDATE"));
        permission.add(new Permission(resource + "_DELETE"));

        roles.get(i).setPermissions(permission);
      } else {
        RBACuserManagementController.log.info(
          "-----> Role has permissions explicitly defined : "
        );
      }
    }

    RBACuserManagementController.log.info(
      "-----> user.getUsername() : " + user.getUsername()
    );

    this.userServiceImpl.createUser(user);
  }

  @GetMapping(value = "/user/{username}")
  public Optional<User> getPersonByUsername(
    @PathVariable(value = "username") String username
  ) {
    RBACuserManagementController.log.info(
      "-----> Getting RBAC User : " + username
    );

    return this.userServiceImpl.getUserByUsername(username);
  }

  @GetMapping(value = "/user")
  public List<User> getusers() {
    RBACuserManagementController.log.info("-----> Getting All RBAC Users.");

    return this.userServiceImpl.getAllUsers();
  }

  @DeleteMapping("/user/{username}")
  public ResponseEntity<?> deletePerson(
    @PathVariable(value = "username") String username
  ) {
    RBACuserManagementController.log.info(
      "-----> Delete RBAC User : " + username
    );

    return this.userServiceImpl.deleteUser(username);
  }
}
