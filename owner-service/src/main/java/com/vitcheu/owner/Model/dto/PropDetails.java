package com.vitcheu.owner.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropDetails {

  @JsonIgnore
  int propId;

  String name;

  PropType type;

  String description;

  int amount;
}
