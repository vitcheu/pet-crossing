package com.vitcheu.authentication;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class AuthenticationApp {

  public static void main(String[] args) {
    SpringApplication.run(AuthenticationApp.class, args);
  }


  @LoadBalanced
  @Bean
  RestTemplate loadBalancedRestTemplate() {
    RestTemplate template = new RestTemplate();
    return template;
  }

  @Bean
  MessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
