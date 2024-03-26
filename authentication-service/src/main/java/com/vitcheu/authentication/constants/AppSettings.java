package com.vitcheu.authentication.constants;

import com.vitcheu.common.utils.Settings;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.settings")
@Data
public class AppSettings implements Settings {

  private String keystoreFileName;
  private String keystoreAlias;
  private String keystorePassword;

  private Long verificationTokenValidity;
  private Long jwtExpirationTime;

  private String mailFrom;
  private String mailReplyTo;
  private String mailSubject;

  private String mailEnvDir;
  private String mailEnvFile;
}
