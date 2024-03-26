package com.vitcheu.owner.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Prop implements java.io.Serializable{
  static final long serialVersionUID = 1L;

  Integer id;

  String name;

  PropType type;

  int quantity;

  String description;

  int price;
}
