package com.vitcheu.common.constants.api.rate_limiting;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class RateLimitingConfiguration implements WebMvcConfigurer {

  private RateLimitInterceptor interceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
      .addInterceptor(this.interceptor)
      .addPathPatterns("/api/v1/person/**", "/management/api/v1/person/**");
  }
}
