package com.vitcheu.common.persistence.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.vitcheu.common.persistence.auditing.AuditEntity;
import com.vitcheu.common.persistence.model.enumeration.ApplicationLogEnum;

@Entity
@Table(name = "sbatlog")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationLog extends AuditEntity {

  //Default Serial Version ID
  private static final long serialVersionUID = 1L;

  @Enumerated(EnumType.STRING)
  @Column(name = "event", nullable = false)
  ApplicationLogEnum event;

  @Column(name = "details", nullable = false)
  String details;
}
