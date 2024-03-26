package com.vitcheu.owner;

import com.vitcheu.owner.context.AppContextHolder;
import com.vitcheu.owner.context.UserContextInterceptor;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAsync
public class OwnerApp {

  public static void main(String[] args) {
    ConfigurableApplicationContext ct = SpringApplication.run(
      OwnerApp.class,
      args
    );
    AppContextHolder.setContext(ct);
  }

  @LoadBalanced
  @Bean
  RestTemplate loadBalancedRestTemplate() {
    RestTemplate template = new RestTemplate();
    var interceptors = template.getInterceptors();
    interceptors.add(new UserContextInterceptor());

    return template;
  }

  @Bean
  Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(25);
    executor.setKeepAliveSeconds(60);
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
    executor.initialize();
    return executor;
  }
}
