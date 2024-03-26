package com.vitcheu.authentication.user.authentication;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class AuthenticationFailureEventListener
  implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private LoginAttemptService loginAttemptService;

  @Override
  public void onApplicationEvent(
    final AuthenticationFailureBadCredentialsEvent e
  ) {
    AuthenticationFailureEventListener.log.info(
      "-----> AuthenticationFailureListener"
    );

    final String xfHeader = this.request.getHeader("X-Forwarded-For");

    //LoginAttemptService is notified of the IP address from where the unsuccessful attempt originated.
    if (xfHeader == null) {
      AuthenticationFailureEventListener.log.info(
        "-----> AuthenticationFailureListener xfHeader == null"
      );

      this.loginAttemptService.loginFailed(this.request.getRemoteAddr());
    } else {
      AuthenticationFailureEventListener.log.info(
        "-----> AuthenticationFailureListener xfHeader != null"
      );

      this.loginAttemptService.loginFailed(xfHeader.split(",")[0]);
    }
  }
}
