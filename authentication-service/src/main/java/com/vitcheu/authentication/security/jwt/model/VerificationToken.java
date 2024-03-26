package com.vitcheu.authentication.security.jwt.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("verification_token")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationToken {


  @Id
  String token;

  Instant expiryDate;

  VerificationTokenEnum status;

  long userId;
}
