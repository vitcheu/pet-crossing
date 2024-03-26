package com.vitcheu.common.persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.vitcheu.common.persistence.auditing.AuditEntity;

@Entity
@Table(name = "sbatsettings")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationSetings extends AuditEntity {

  private static final long serialVersionUID = 1L;

  @Size(
    min = 3,
    max = 25,
    message = "App Key must be between 3 and 25 characters."
  )
  @Column(name = "app_key", nullable = false)
  String appKey;

  @Size(
    min = 3,
    max = 100,
    message = "App Value must be between 3 and 100 characters."
  )
  @Column(name = "app_value", nullable = false)
  String appValue;
}
