package com.vitcheu.authentication.user.model;

import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.vitcheu.common.persistence.auditing.AuditEntity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "auth_role")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Role extends AuditEntity {

  private static final long serialVersionUID = 1L;

  @Column(name = "name")
  String name;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
    name = "permission_role",
    joinColumns = {
      @JoinColumn(name = "role_id", referencedColumnName = "id"),
    },
    inverseJoinColumns = {
      @JoinColumn(name = "permission_id", referencedColumnName = "id"),
    }
  )
  private List<Permission> permissions;
}
