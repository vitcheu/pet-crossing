package com.vitcheu.gateway.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "app.settings")
@Data
public class AppSettings {

  private String keystoreFileName;
  private String keystoreAlias;
  private String keystorePassword;
  private boolean debug;

}
