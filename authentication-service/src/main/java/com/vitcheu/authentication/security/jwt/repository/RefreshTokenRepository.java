package com.vitcheu.authentication.security.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vitcheu.authentication.security.jwt.model.RefreshToken;

@Repository
public interface RefreshTokenRepository
  extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  Optional<RefreshToken> findByUsername(String username);

  void deleteByToken(String token);
}
