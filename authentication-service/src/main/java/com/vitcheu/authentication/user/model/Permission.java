package com.vitcheu.authentication.user.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.vitcheu.common.persistence.auditing.AuditEntity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "auth_permission")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Permission extends AuditEntity {

  private static final long serialVersionUID = 1L;

  @Column(name = "name")
  String name;
}
