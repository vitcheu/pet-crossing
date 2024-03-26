package com.vitcheu.authentication.security.jwt.model;

import com.vitcheu.common.persistence.auditing.AuditEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Table(name = "refresh_token")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshToken extends AuditEntity {

  private static final long serialVersionUID = 1L;

  @Column(name = "token")
  String token;

  @Column(name = "username")
  String username;
}
