package com.vitcheu.common.service.impl;

import com.vitcheu.common.persistence.model.ApplicationSetings;
import com.vitcheu.common.persistence.repositories.ApplicationSettingsRepository;
import com.vitcheu.common.service.OtherService;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Getter
@Setter
@Service
public class OtherServicesImpl implements OtherService {

  //Application, API details
  private String applicationName;
  private String applicationVersion;
  private String apiVersion;
  private String releaseVersion;

  //Postman URL
  private String postmanEchoBaseUrl;
  private String postmanEchoGETurl;
  private String postmanEchoPOSTpath;

  private String keystoreFileName;
  private String keystoreAlias;
  private String keystorePassword;

  private Long verificationTokenValidity;
  private Long jwtExpirationTime;

  private String mailFrom;
  private String mailReplyTo;
  private String mailSubject;

  @Autowired
  private ApplicationSettingsRepository applicationSettingsRepository;

  @Override
  public void loadApplicationSettings() {
    OtherServicesImpl.log.info("-----> Loading Application Settings Value");

    List<ApplicationSetings> applicationSettingsList =
      this.applicationSettingsRepository.findAll();

    HashMap<String, String> applicationSettingsHashMap = new HashMap<>();

    for (int i = 0; i < applicationSettingsList.size(); i++) {
      applicationSettingsHashMap.put(
        applicationSettingsList.get(i).getAppKey(),
        applicationSettingsList.get(i).getAppValue()
      );
    }

    this.setApplicationName(applicationSettingsHashMap.get("applicationName"));
    this.setApplicationVersion(
        applicationSettingsHashMap.get("applicationVersion")
      );
    this.setApiVersion(applicationSettingsHashMap.get("apiVersion"));
    this.setReleaseVersion(applicationSettingsHashMap.get("releaseVersion"));

    this.setPostmanEchoBaseUrl(
        applicationSettingsHashMap.get("postmanEchoBaseUrl")
      );
    this.setPostmanEchoGETurl(
        applicationSettingsHashMap.get("postmanEchoGETurl")
      );
    this.setPostmanEchoPOSTpath(
        applicationSettingsHashMap.get("postmanEchoPOSTpath")
      );

    this.setKeystoreFileName(
        applicationSettingsHashMap.get("keystoreFileName")
      );
    this.setKeystoreAlias(applicationSettingsHashMap.get("keystoreAlias"));
    this.setKeystorePassword(
        applicationSettingsHashMap.get("keystorePassword")
      );

    this.setVerificationTokenValidity(
        Long.parseLong(
          applicationSettingsHashMap.get("verificationTokenValidity")
        )
      );
    this.setJwtExpirationTime(
        Long.parseLong(applicationSettingsHashMap.get("jwtExpirationTime"))
      );

    this.setMailFrom(applicationSettingsHashMap.get("mailFrom"));
    this.setMailReplyTo(applicationSettingsHashMap.get("mailReplyTo"));
    this.setMailSubject(applicationSettingsHashMap.get("mailSubject"));
  }
}
