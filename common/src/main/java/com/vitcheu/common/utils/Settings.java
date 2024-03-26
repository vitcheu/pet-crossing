package com.vitcheu.common.utils;


public interface Settings {
  public String getKeystoreFileName();


  public String getKeystoreAlias();


  public String getKeystorePassword();


  public Long getVerificationTokenValidity();


  public Long getJwtExpirationTime();


  public String getMailFrom();


  public String getMailReplyTo();


  public String getMailSubject();

}
