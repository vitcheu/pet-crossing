package com.vitcheu.authentication.user;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.vitcheu.authentication.user.model.Role;
import com.vitcheu.authentication.user.model.User;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class UserPrincipal implements UserDetails {

  private static final long serialVersionUID = 1L;

  private final User user;
  private final Collection<? extends GrantedAuthority> authorities;

  public UserPrincipal(User user) {
    UserPrincipal.log.info(
      "-----> username            : " + user.getUsername()
    );
    this.user = user;

    Set<String> roleAndPermissions = new HashSet<>();
    List<Role> roles = user.getRoles();
    for (Role role : roles) {
      roleAndPermissions.add(role.getName());

      UserPrincipal.log.info("-----> Role                : " + role.getName());

      for (int i = 0; i < role.getPermissions().size(); i++) {
        String name = role.getPermissions().get(i).getName();
        UserPrincipal.log.info("-----> Permission          : " + name);
        roleAndPermissions.add(name);
      }
    }

    String[] roleNames = new String[roleAndPermissions.size()];

    UserPrincipal.log.info(
      "-----> Roles & Permissions : " + roleAndPermissions.toString()
    );
    this.authorities =
      AuthorityUtils.createAuthorityList(roleAndPermissions.toArray(roleNames));
  }

  public User getUser() {
    return this.user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return this.user.getPassword();
  }

  @Override
  public String getUsername() {
    return this.user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
