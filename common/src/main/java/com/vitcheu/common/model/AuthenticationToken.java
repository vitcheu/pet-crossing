package com.vitcheu.common.model;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import lombok.Builder.Default;

/**
 * Represents the authentication token used to 
 * send to users who login and passed between backend services;
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationToken {

  private long userId;
  private String name;
  @Default
  private List<String> authorities = new ArrayList<>();
}
