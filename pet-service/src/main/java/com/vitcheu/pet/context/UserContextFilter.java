package com.vitcheu.pet.context;

import com.vitcheu.common.constants.api.filter.Headers;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserContextFilter implements Filter {

  private static final Logger logger = LoggerFactory.getLogger(
    UserContextFilter.class
  );

  @Override
  public void doFilter(
    ServletRequest servletRequest,
    ServletResponse servletResponse,
    FilterChain filterChain
  ) throws IOException, ServletException {
    var req = (HttpServletRequest) servletRequest;

    String correlation_id = req.getHeader(Headers.CORRELATION_ID);
    UserContext.setCorrelationId(correlation_id);
    UserContext.setUserId(req.getHeader(UserContext.USER_ID));
    UserContext.setAuthToken(req.getHeader(Headers.AUTH_TOKEN));
    UserContext.setOrganizationId(req.getHeader(UserContext.ORGANIZATION_ID));

    logger.info(
      "UserContextFilter Correlation id: {}",
       correlation_id
    );

    filterChain.doFilter(req, servletResponse);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void destroy() {}
}
