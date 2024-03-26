package com.vitcheu.common.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vitcheu.common.persistence.auditing.AuditEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "sbatbuilt_with")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuiltWith extends AuditEntity {

  //Default Serial Version ID
  private static final long serialVersionUID = 1L;

  @Size(
    min = 3,
    max = 15,
    message = "name must be between 3 and 15 characters."
  )
  @Column(name = "name", nullable = false)
  String name;

  @Column(name = "version", nullable = true)
  String version;

  @Column(name = "description", nullable = true)
  String description;

  @Column(name = "link", nullable = true)
  String link;
}
