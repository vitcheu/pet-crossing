package com.vitcheu.gateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Order(5)
@Slf4j
public class StatisticFilter implements GlobalFilter {
  @Autowired
  FilterUtils filterUtils;


  @Override
  public Mono<Void> filter(
    ServerWebExchange exchange,
    GatewayFilterChain chain
  ) {
    exchange
      .getAttributes()
      .put(FilterUtils.START_TIME, System.currentTimeMillis());
    return chain
      .filter(exchange)
      .then(
        Mono.fromRunnable(() -> {
          Long startTime = exchange.getAttribute(FilterUtils.START_TIME);
          if (startTime != null) {
            ServerHttpRequest req = exchange.getRequest();
            log.info("\n--------------------------------------------");
            log.info(
              "sevice time:\t" + (System.currentTimeMillis() - startTime)
            );
            log.info("url:\t" + req.getURI());
            log.info("req.getLocalAddress(): " + req.getLocalAddress());
            log.info("--------------------------------------------\n");
          }
        })
      );
  }

}
