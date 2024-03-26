package com.vitcheu.authentication.security.jwt.service;

import com.vitcheu.common.exception.OtherExceptions;
import com.vitcheu.common.utils.Settings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  @Autowired
  private Settings settings;

  private KeyStore keyStore;

  public JwtProvider(Settings settings) {
    this.settings = settings;
  }

  @PostConstruct
  public void init() {
    try {
      this.keyStore = KeyStore.getInstance("JKS");

      InputStream resourceAsStream =
        this.getClass()
          .getResourceAsStream("/" + this.settings.getKeystoreFileName());

      this.keyStore.load(
          resourceAsStream,
          this.settings.getKeystorePassword().toCharArray()
        );
    } catch (
      KeyStoreException
      | CertificateException
      | NoSuchAlgorithmException
      | IOException e
    ) {
      throw new OtherExceptions("Exception occurred while loading keystore", e);
    }
  }

  private PrivateKey getPrivateKey() {
    try {
      PrivateKey key = (PrivateKey) this.keyStore.getKey(
          this.settings.getKeystoreAlias(),
          this.settings.getKeystorePassword().toCharArray()
        );
      if (key == null) {
        throw new KeyStoreException("could not get key");
      }
      return key;
    } catch (
      KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e
    ) {
      throw new OtherExceptions(
        "Exception occured while retrieving public key from keystore",
        e
      );
    }
  }

  private PublicKey getPublickey() {
    try {
      return this.keyStore.getCertificate(this.settings.getKeystoreAlias())
        .getPublicKey();
    } catch (KeyStoreException e) {
      throw new OtherExceptions(
        "Exception occured while retrieving public key from keystore",
        e
      );
    }
  }

  public String generateToken(
    Authentication authentication,
    Instant expiration
  ) {
    return Jwts
      .builder()
      .setSubject(authentication.getName())
      .signWith(this.getPrivateKey())
      .setExpiration(Date.from(expiration))
      .compact();
  }

  public String generateTokenWithUserName(String username) {
    return Jwts
      .builder()
      .setSubject(username)
      .setIssuedAt(Date.from(Instant.now()))
      .signWith(this.getPrivateKey())
      .setExpiration(
        Date.from(
          Instant.now().plusSeconds(this.settings.getJwtExpirationTime())
        )
      )
      .compact();
  }

  public boolean validateToken(String jwt) {
    Jwts
      .parserBuilder()
      .setSigningKey(this.getPublickey())
      .build()
      .parseClaimsJws(jwt);

    return true;
  }

  public String getUsernameFromJwt(String token) {
    Claims claims = Jwts
      .parserBuilder()
      .setSigningKey(this.getPublickey())
      .build()
      .parseClaimsJws(token)
      .getBody();

    return claims.getSubject();
  }
}
