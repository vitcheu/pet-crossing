package com.vitcheu.gateway.filters;

import static com.vitcheu.common.constants.api.filter.Headers.USER_ID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.vitcheu.common.exception.UnAuthorizedException;
import com.vitcheu.common.model.AuthenticationToken;
import com.vitcheu.gateway.application.AppSettings;
import com.vitcheu.gateway.service.AuthenticationService;
import com.vitcheu.gateway.utils.JwtDecoder;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Order(2)
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter {

  private static final String BEARER_PREFIX = "Bearer ";

  @Autowired
  FilterUtils filterUtils;

  @Autowired
  JwtDecoder jwtDecoder;

  @Autowired
  AuthenticationService authenticationService;

  @Autowired
  AppSettings settings;

  @Override
  public Mono<Void> filter(
    ServerWebExchange exchange,
    GatewayFilterChain chain
  ) {
    if (isLoginRequest(exchange)) {
      return chain.filter(exchange);
    }

    HttpHeaders requestHeaders = exchange.getRequest().getHeaders();

    var agent = filterUtils.getHeader(requestHeaders, "User-Agent");
    log.info("\n------------------------------------------------");
    log.info("agent: " + agent.map(a -> a.toString()).orElse(null));

    var jwtToken = filterUtils.getAuthToken(requestHeaders);

    AuthenticationToken auth_token = null;
    try {
      auth_token =
        jwtToken
          .map(this::loadAuthenticationToken)
          .orElseThrow(() -> new UnAuthorizedException("No token found"));
      var name=auth_token.getName();
      assert name != null;

      log.info("Authentication name:" + name);
      setUserIdFromToken(exchange, auth_token);

      return chain.filter(exchange);
    } catch (UnAuthorizedException e) {
      log.error(e.getMessage());
      if (settings.isDebug()) {
        e.printStackTrace();
        var userId = filterUtils.getHeader(requestHeaders, USER_ID);
        log.info("userId: " + userId.orElse(null));
        return chain.filter(exchange);
      } else {
        throw e;
      }
    } finally {
      log.info("------------------------------------------------\n");
    }
  }

  private void setUserIdFromToken(
    @NonNull ServerWebExchange exchange,
    @NonNull AuthenticationToken auth
  ) {
    var userId = auth.getUserId();
    log.info("Setting userId: " + userId + " in headers");
    filterUtils.setUserId(exchange, userId);
  }

  private boolean isLoginRequest(ServerWebExchange exchange) {
    ServerHttpRequest req = exchange.getRequest();
    String url = req.getURI().toString();
    return (
      (url.contains("/login")) ||
      (url.contains("verification")) &&
      req.getMethod().equals(org.springframework.http.HttpMethod.POST)
    );
  }

  private @NonNull AuthenticationToken loadAuthenticationToken(
    @NonNull String jwtToken
  ) {
    try {
      if (jwtToken.startsWith(BEARER_PREFIX)) {
        jwtToken = jwtToken.replace(BEARER_PREFIX, "");
        jwtDecoder.validateToken(jwtToken);

        String username = jwtDecoder.getUsernameFromJwt(jwtToken);
        var authToken = authenticationService.loadAuthentication(username);

        return authToken;
      } else {
        throw new RuntimeException("Invalid token format: " + jwtToken);
      }
    } catch (Exception e) {
      throw new UnAuthorizedException("Invalid token: " + e.getMessage(), e);
    }
  }
}
