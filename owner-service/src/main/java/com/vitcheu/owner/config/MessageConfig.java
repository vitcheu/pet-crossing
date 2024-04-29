package com.vitcheu.owner.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {

  @Bean
  MessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
