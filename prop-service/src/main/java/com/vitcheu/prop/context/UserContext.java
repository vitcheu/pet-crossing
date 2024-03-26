package com.vitcheu.prop.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserContext {

  public static final String CORRELATION_ID = "tmx-correlation-id";
  public static final String ORGANIZATION_ID = "tmx-organization-id";

  private static final ThreadLocal<String> correlationId = new ThreadLocal<String>();
  private static final ThreadLocal<String> authToken = new ThreadLocal<String>();
  private static final ThreadLocal<String> userId = new ThreadLocal<String>();
  private static final ThreadLocal<String> organizationId = new ThreadLocal<String>();

  public static String getCorrelationId() {
    return correlationId.get();
  }

  public static void setCorrelationId(String cid) {
    correlationId.set(cid);
  }

  public static String getAuthToken() {
    return authToken.get();
  }

  public static void setAuthToken(String aToken) {
    authToken.set(aToken);
  }

  public static void setUserId(String aUser) {
    userId.set(aUser);
  }

  public static String getOrganizationId() {
    return organizationId.get();
  }

  public static void setOrganizationId(String organization) {
    organizationId.set(organization);
  }

  public static HttpHeaders getHttpHeaders() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set(CORRELATION_ID, getCorrelationId());

    return httpHeaders;
  }

  public static long getUserId() {
    var id = userId.get();
    log.info("User Id: "+id);
    return Long.parseLong(id);
  }

  private static String getUserIdStr() {
    return userId.get();
  }
}
