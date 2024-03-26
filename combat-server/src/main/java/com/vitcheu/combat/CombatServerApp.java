package com.vitcheu.combat;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import com.vitcheu.combat.context.AppContextHolder;
import com.vitcheu.combat.context.ContextInterceptor;

@EnableDiscoveryClient
@SpringBootApplication
@EnableAsync()
public class CombatServerApp {

  public static void main(String[] args) {
    var context = SpringApplication.run(CombatServerApp.class, args);
    AppContextHolder.setAppContext(context);
  }

  @Bean
  @LoadBalanced
  RestTemplate loadBalancedRestTemplate() {
    RestTemplate template = new RestTemplate();
    var interceptors = template.getInterceptors();
    interceptors.add(new ContextInterceptor());
    return template;
  }

  @Bean(name = "taskExecutor")
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
