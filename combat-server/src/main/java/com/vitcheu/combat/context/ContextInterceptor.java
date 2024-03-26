package com.vitcheu.combat.context;

import static com.vitcheu.common.constants.api.filter.Headers.USER_ID;

import com.vitcheu.common.constants.api.filter.Headers;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.lang.NonNull;

@Slf4j
public class ContextInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public @NonNull ClientHttpResponse intercept(
    @NonNull HttpRequest request,
    @NonNull byte[] body,
    @NonNull ClientHttpRequestExecution execution
  ) throws IOException {
    HttpHeaders headers = request.getHeaders();
    long userId = PlayerContextHoler.getUserId();
    log.info(
      "ContextInterceptor.intercept()\nAdding header:(%s:%s)".formatted(
          USER_ID,
          userId
        )
    );
    headers.add(Headers.USER_ID, String.valueOf(userId));
    return execution.execute(request, body);
  }
}
