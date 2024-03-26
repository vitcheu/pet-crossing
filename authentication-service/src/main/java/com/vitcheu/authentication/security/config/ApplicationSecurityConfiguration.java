package com.vitcheu.authentication.security.config;

import static com.vitcheu.common.constants.api.ResourcePaths.Authentication.V1.*;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.vitcheu.authentication.security.jwt.service.JwtAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration {

  private final UserDetailsService userDetailsService;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  /** Public URLs. */
  private static final String[] PUBLIC_MATCHERS = {
    "/webjars/**",
    "/error/**",
    "/lang",
    "/h2-console/**",
    ROOT + LOGIN,
    ROOT+LOGOUT,
    ROOT + SIGNUP,
    ROOT+"/verification/**",
  };

  // private static final String[] SWAGGER_MATCHERS = {
  //   "/v2/api-docs",
  //   "/api/v2/api-docs",
  //   "/configuration/ui",
  //   "/swagger-resources/**",
  //   "/configuration/security",
  //   "/swagger-ui.html",
  //   "/webjars/**",
  // };

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(t -> t.disable())
      .authorizeHttpRequests(auth ->
        auth
          .requestMatchers(ApplicationSecurityConfiguration.PUBLIC_MATCHERS)
          .permitAll()
          .requestMatchers("/rbac/**")
          .hasRole("ADMIN")
          .anyRequest()
          .authenticated()
      )
      .httpBasic(Customizer.withDefaults())
      .exceptionHandling(handler -> handler.accessDeniedPage("/403"));

    http
      .addFilterBefore(
        jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter.class
      )
      //https://stackoverflow.com/questions/53395200/h2-console-is-not-showing-in-browser
      // .headers(headers -> headers.frameOptions(option -> option.sameOrigin()))
      .sessionManagement(management ->
        management
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .maximumSessions(1)
          .sessionRegistry(this.sessionRegistry())
      );

    return http.build();
  }

  @Bean
  SessionRegistryImpl sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
    return new ServletListenerRegistrationBean<>(
      new HttpSessionEventPublisher()
    );
  }

  @Bean
  ProviderManager authenticationManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(this.passwordEncoder());
    ProviderManager providerManager = new ProviderManager(provider);
    return providerManager;
  }

  @Bean
  BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }
}
