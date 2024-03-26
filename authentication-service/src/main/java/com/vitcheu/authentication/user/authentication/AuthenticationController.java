package com.vitcheu.authentication.user.authentication;

import static org.springframework.http.HttpStatus.OK;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.vitcheu.authentication.security.jwt.model.*;
import com.vitcheu.authentication.security.jwt.service.RefreshTokenServiceImpl;
import com.vitcheu.authentication.user.model.UserLogin;
import com.vitcheu.authentication.user.model.UserSignup;
import com.vitcheu.common.constants.api.ResourcePaths;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = ResourcePaths.Authentication.V1.ROOT)
@AllArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;
  private final RefreshTokenServiceImpl refreshTokenServiceImpl;
  private final ModelMapper modelMapper;

  @PostMapping(value = ResourcePaths.Authentication.V1.SIGNUP)
  public ResponseEntity<String> signup(@RequestBody UserSignup userSignup) {
    this.authenticationService.signup(userSignup);

    return ResponseEntity
      .status(OK)
      .body(
        "User Registration Successful! Activate your account by following the instructions in the verification email."
      );
  }

  @GetMapping(value = ResourcePaths.Authentication.V1.VERIFICATION)
  public ResponseEntity<String> verifyAccount(@PathVariable String token) {
    return new ResponseEntity<>(
      this.authenticationService.verifyAccount(token),
      OK
    );
  }

  @PostMapping(value = ResourcePaths.Authentication.V1.LOGIN)
  public AuthenticationResponse login(@RequestBody UserLogin userLogin) {
    return this.authenticationService.login(userLogin);
  }

  @PostMapping(value = ResourcePaths.Authentication.V1.REFRESH_TOKEN)
  public AuthenticationResponse refreshTokens(
    @Valid @RequestBody RefreshTokenDTO refreshTokenDTO
  ) {
    RefreshToken refreshToken =
      this.modelMapper.map(refreshTokenDTO, RefreshToken.class);
    return this.authenticationService.refreshToken(refreshToken);
  }

  @PostMapping(value = ResourcePaths.Authentication.V1.LOGOUT)
  public ResponseEntity<String> logout(
    @RequestBody RefreshTokenDTO refreshTokenDTO
  ) {
    this.refreshTokenServiceImpl.deleteByToken(
        refreshTokenDTO.getToken(),
        refreshTokenDTO.getUsername()
      );

    return ResponseEntity
      .status(OK)
      .body("Refresh Token Deleted Successfully.");
  }

  @GetMapping("/principle")
  public ResponseEntity<Authentication> currentPrinciple(
    Authentication authentication
  ) {
    return ResponseEntity.status(OK).body(authentication);
  }
}
