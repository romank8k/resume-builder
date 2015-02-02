package me.romankh.resumegenerator.web.filter;

import com.google.inject.Singleton;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Roman Khmelichek
 */
@Singleton
public class Utf8OutputFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    // Must set this prior to HttpServletResponse.getWriter() being called in order to properly set the output encoding.
    response.setCharacterEncoding("UTF-8");
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
  }
}
