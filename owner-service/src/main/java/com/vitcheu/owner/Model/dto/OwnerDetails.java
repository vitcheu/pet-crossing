package com.vitcheu.owner.model.dto;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
public class OwnerDetails {

  long userId;

  String username;

  String email;

  int money;

  List<PropDetails> props;
}
