package com.vitcheu.authentication.security.jwt.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vitcheu.authentication.security.jwt.model.VerificationToken;

@Repository
public interface VerificationTokenRepository
  extends CrudRepository<VerificationToken, String> {
}
