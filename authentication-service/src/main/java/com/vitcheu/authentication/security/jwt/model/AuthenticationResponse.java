package com.vitcheu.authentication.security.jwt.model;

import java.time.ZonedDateTime;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  private String authenticationToken;
  private String refreshToken;
  private ZonedDateTime expiresAt;
  private String username;
}
