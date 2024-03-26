package com.vitcheu.owner.context;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.lang.NonNull;

import com.vitcheu.common.constants.api.filter.Headers;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserContextInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public @NonNull ClientHttpResponse intercept(
    @NonNull HttpRequest request,
    @NonNull byte[] body,
    @NonNull ClientHttpRequestExecution execution
  ) throws IOException {
    HttpHeaders headers = request.getHeaders();
    String correlationId = UserContextHolder.getContext().getCorrelationId();
    String authToken = UserContextHolder.getContext().getAuthToken();
    log.info("\nUserContextInterceptor.intercept()--------------------------");
    log.info("correlationId: "+correlationId);
    log.info("authToken: "+authToken);
    
    headers.add(
      UserContext.CORRELATION_ID,
      correlationId
    );
    headers.add(
      Headers.AUTH_TOKEN,
      authToken
    );

    log.info("UserContextInterceptor.intercept()--------------------------\n");
    return execution.execute(request, body);
  }
}
