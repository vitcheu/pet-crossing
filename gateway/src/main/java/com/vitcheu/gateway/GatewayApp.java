package com.vitcheu.gateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import java.time.Duration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.*;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApp {

  public static void main(String[] args) {
    SpringApplication.run(GatewayApp.class, args);
  }

  @Bean
  @LoadBalanced
  RestTemplate loadBalancedRestTemplate() {
    return new RestTemplate();
  }

  @Bean
  @LoadBalanced
  public WebClient.Builder loadBalancedWebClientBuilder() {
    return WebClient.builder();
  }

  @Bean
  RouterFunction<?> routerFunction() {
    RouterFunction router = RouterFunctions.route(
      RequestPredicates.GET("/"),
      request ->
        ServerResponse
          .ok()
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue("Ok! Connected.")
    );
    return router;
  }

  /**
   * Default Resilience4j circuit breaker configuration
   */
  @Bean
  public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
    return factory ->
      factory.configureDefault(id ->
        new Resilience4JConfigBuilder(id)
          .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
          .timeLimiterConfig(
            TimeLimiterConfig
              .custom()
              .timeoutDuration(Duration.ofSeconds(4))
              .build()
          )
          .build()
      );
  }
}
