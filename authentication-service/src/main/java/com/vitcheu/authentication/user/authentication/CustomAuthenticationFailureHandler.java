package com.vitcheu.authentication.user.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

@Log4j2
@Component("authenticationFailureHandler")
public class CustomAuthenticationFailureHandler
  extends SimpleUrlAuthenticationFailureHandler {

  @Autowired
  private MessageSource messages;

  @Autowired
  private LocaleResolver localeResolver;

  @Override
  public void onAuthenticationFailure(
    final HttpServletRequest request,
    final HttpServletResponse response,
    final AuthenticationException exception
  ) {
    this.setDefaultFailureUrl("/login?error=true");

    try {
      super.onAuthenticationFailure(request, response, exception);
    } catch (IOException | ServletException e) {
      CustomAuthenticationFailureHandler.log.error(
        "onAuthenticationFailure",
        e
      );
    }

    final Locale locale = this.localeResolver.resolveLocale(request);

    String errorMessage =
      this.messages.getMessage("message.badCredentials", null, locale);

    if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
      errorMessage =
        this.messages.getMessage("auth.message.disabled", null, locale);
    } else if (
      exception.getMessage().equalsIgnoreCase("User account has expired")
    ) {
      errorMessage =
        this.messages.getMessage("auth.message.expired", null, locale);
    } else if (exception.getMessage().equalsIgnoreCase("blocked")) {
      errorMessage =
        this.messages.getMessage("auth.message.blocked", null, locale);
    } else if (exception.getMessage().equalsIgnoreCase("unusual location")) {
      errorMessage =
        this.messages.getMessage("auth.message.unusual.location", null, locale);
    }

    request
      .getSession()
      .setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
  }
}
