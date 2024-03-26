package com.vitcheu.owner.context;

import com.vitcheu.common.constants.api.filter.Headers;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserContextFilter implements Filter {

  @Override
  public void doFilter(
    ServletRequest servletRequest,
    ServletResponse servletResponse,
    FilterChain filterChain
  ) throws IOException, ServletException {
    var req = (HttpServletRequest) servletRequest;

    String userId = req.getHeader(Headers.USER_ID);
    String auth = req.getHeader(Headers.AUTH_TOKEN);
    UserContext.setUserId(userId);
    UserContext.setAuthToken(auth);

    log.debug(String.format("\nUserId:%s", userId));

    filterChain.doFilter(req, servletResponse);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void destroy() {}
}
