package com.vitcheu.gateway.filters;

import static com.vitcheu.common.constants.api.filter.Headers.*;

import com.vitcheu.common.constants.api.filter.Headers;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class FilterUtils {

  public static final String PRE_FILTER_TYPE = "pre";
  public static final String POST_FILTER_TYPE = "post";
  public static final String ROUTE_FILTER_TYPE = "route";
  public static final String START_TIME = "start-time";

  public Optional<String> getCorrelationId(HttpHeaders requestHeaders) {
    return getHeader(requestHeaders, CORRELATION_ID);
  }

  public Optional<String> getAuthToken(HttpHeaders requestHeaders) {
    return getHeader(requestHeaders, AUTH_TOKEN);
  }

  public Optional<String> getHeader(
    HttpHeaders requestHeaders,
    String headerName
  ) {
    List<String> list = requestHeaders.get(headerName);
    if (list != null) {
      return list.stream().findFirst();
    } else {
      return Optional.empty();
    }
  }

  public ServerWebExchange setRequestHeader(
    ServerWebExchange exchange,
    String name,
    String value
  ) {
    return exchange
      .mutate()
      .request(exchange.getRequest().mutate().header(name, value).build())
      .build();
  }

  public ServerWebExchange setCorrelationId(
    ServerWebExchange exchange,
    String correlationId
  ) {
    return this.setRequestHeader(exchange, CORRELATION_ID, correlationId);
  }

  public ServerWebExchange setAuthentication(
    ServerWebExchange exchange,
    String authentication
  ) {
    return this.setRequestHeader(exchange, Headers.AUTH_TOKEN, authentication);
  }

  public ServerWebExchange setUserId(ServerWebExchange exchange, Long userId) {
    return this.setRequestHeader(exchange, USER_ID, String.valueOf(userId));
  }
}
