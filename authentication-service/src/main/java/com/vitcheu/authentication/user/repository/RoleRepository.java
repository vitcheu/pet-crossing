package com.vitcheu.authentication.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vitcheu.authentication.user.model.Permission;
import com.vitcheu.authentication.user.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  List<Permission> findByName(String roleName);
}
