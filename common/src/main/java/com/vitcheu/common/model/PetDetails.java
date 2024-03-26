package com.vitcheu.common.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class PetDetails implements java.io.Serializable{
  private static final long serialVersionUID = 1L;

  Integer id;

  String name;

  long ownerId;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
   @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  Date birthDate;

  String type;

  PetProperties properties;
}
