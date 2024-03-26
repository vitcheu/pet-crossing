package com.vitcheu.common.model.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Size;

public record PetRequest(
  int id,
  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8") Date birthDate,
  @Size(min = 1) String name,
  int typeId
) {}
