package com.vitcheu.authentication.security.jwt.service;

import com.vitcheu.authentication.security.jwt.model.RefreshToken;

public interface RefreshTokenService {
  RefreshToken generateRefreshToken(String stage, String username);

  void validateRefreshToken(RefreshToken refreshToken);

  void deleteByToken(String token, String username);
}
