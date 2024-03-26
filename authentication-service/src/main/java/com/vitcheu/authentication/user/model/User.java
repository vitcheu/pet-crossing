package com.vitcheu.authentication.user.model;

import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.vitcheu.common.persistence.auditing.AuditEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "t_user")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class User extends AuditEntity {

  private static final long serialVersionUID = 1L;

  @Column(name = "password")
  String password;

  @Column(name = "username", unique = true)
  @Size(max = 255, message = "username must not be empty.")
  String username;

  @Email
  @NotEmpty(message = "Email is required")
  @Column(name = "email", unique = true, nullable = false)
  String email;

  @Column(name = "isAccountNonExpired")
  boolean isAccountNonExpired;

  @Column(name = "isAccountNonLocked")
  boolean isAccountNonLocked;

  @Column(name = "isCredentialsNonExpired")
  boolean isCredentialsNonExpired;

  @Column(name = "isEnabled")
  boolean isEnabled;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
    name = "auth_role_user",
    joinColumns = {
      @JoinColumn(name = "user_id", referencedColumnName = "id"),
    },
    inverseJoinColumns = {
      @JoinColumn(name = "role_id", referencedColumnName = "id"),
    }
  )
  private List<Role> roles;
}
