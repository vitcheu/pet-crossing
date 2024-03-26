package com.vitcheu.gateway.filters;

import static com.vitcheu.common.constants.api.filter.Headers.AUTH_TOKEN;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import brave.Tracer;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@Profile("dev")
public class ResponseFilter {

  final Logger logger = LoggerFactory.getLogger(ResponseFilter.class);

  @Autowired
  Tracer tracer;

  @Autowired
  FilterUtils filterUtils;

  @Bean
  public GlobalFilter postGlobalFilter() {
    return (exchange, chain) -> {
      return chain
        .filter(exchange)
        .then(
          Mono.fromRunnable(() -> {
            setResponseHeader(exchange, AUTH_TOKEN);
          })
        );
    };
  }

  private void setResponseHeader(
    ServerWebExchange exchange,
    String headerName
  ) {
    ServerHttpRequest req = exchange.getRequest();
    HttpHeaders requestHeaders = req.getHeaders();
    List<String> list = requestHeaders.get(headerName);

    if (list != null && !list.isEmpty()) {
      String value = list.get(0);
      log.info("setResponceHeader, %s = %s ".formatted(headerName, value));
      exchange.getResponse().getHeaders().add(headerName, value);
    }
  }
}
