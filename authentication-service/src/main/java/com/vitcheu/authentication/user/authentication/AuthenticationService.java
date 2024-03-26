package com.vitcheu.authentication.user.authentication;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.vitcheu.authentication.security.jwt.model.*;
import com.vitcheu.authentication.user.model.*;

public interface AuthenticationService {
  void signup(UserSignup userSignup);

  void fetchUserAndEnable(VerificationToken verificationToken);

  String verifyAccount(@PathVariable String token);

  String generateVerificationToken(User user);

  AuthenticationResponse login(@RequestBody UserLogin userLogin);

  AuthenticationResponse refreshToken(RefreshToken refreshToken);
}
