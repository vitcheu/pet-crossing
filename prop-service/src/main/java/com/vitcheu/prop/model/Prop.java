package com.vitcheu.prop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "props")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Prop implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  String name;

  @Column(name = "type_id")
  @Enumerated(value = EnumType.ORDINAL)
  PropType type;

  @JsonIgnore
  int quantity;

  String description;

  int price;
}
