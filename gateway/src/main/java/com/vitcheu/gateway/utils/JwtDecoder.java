package com.vitcheu.gateway.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vitcheu.common.exception.OtherExceptions;
import com.vitcheu.gateway.application.AppSettings;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;

@Component
public class JwtDecoder {

  @Autowired
  private AppSettings settings;

  private KeyStore keyStore;

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
