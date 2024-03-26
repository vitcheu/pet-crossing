package com.vitcheu.authentication.user.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class AuthenticationSuccessEventListener
  implements ApplicationListener<AuthenticationSuccessEvent> {

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private LoginAttemptService loginAttemptService;

  @Override
  public void onApplicationEvent(final AuthenticationSuccessEvent e) {
    AuthenticationSuccessEventListener.log.info(
      "-----> AuthenticationSuccessEventListener"
    );

    final String xfHeader = this.request.getHeader("X-Forwarded-For");

    //LoginAttemptService is notified of the IP address from where the successful attempt originated.
    if (xfHeader == null) {
      AuthenticationSuccessEventListener.log.info(
        "-----> AuthenticationSuccessEventListener : xfHeader == null"
      );

      this.loginAttemptService.loginSucceeded(this.request.getRemoteAddr());
    } else {
      AuthenticationSuccessEventListener.log.info(
        "-----> AuthenticationSuccessEventListener : xfHeader != null"
      );

      this.loginAttemptService.loginSucceeded(xfHeader.split(",")[0]);
    }
  }
}
