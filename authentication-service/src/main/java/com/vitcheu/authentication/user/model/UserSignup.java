package com.vitcheu.authentication.user.model;

import com.vitcheu.common.persistence.auditing.AuditEntity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class UserSignup extends AuditEntity {

  private static final long serialVersionUID = 1L;

  String email;
  String username;
  String password;
}
