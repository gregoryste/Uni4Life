package br.edu.unisinos.uni4life.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@WebFilter("/*")
public final class CORSFilter implements Filter {

   public CORSFilter() {
   }

   @Override
   public void init(final FilterConfig fc) throws ServletException {
   }

   @Override
   public void doFilter(final ServletRequest req, final ServletResponse resp,
                        final FilterChain chain) throws IOException, ServletException {

      final HttpServletResponse response = (HttpServletResponse) resp;
      final HttpServletRequest request = (HttpServletRequest) req;
      response.setHeader("Access-Control-Allow-Origin", "*");
      response.setHeader("Access-Control-Allow-Credentials", "true");
      response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, OPTIONS, DELETE, PUT");
      response.setHeader("Access-Control-Max-Age", "3600");
      response.setHeader("Access-Control-Allow-Headers", "Origin, origin," +
          " x-requested-with, Content-Type, content-type," +
          " Authorization, authorization," +
          " credential, X-XSRF-TOKEN," +
          " Access-Control-Allow-Methods, access-control-allow-methods, " +
          " Access-Control-Allow-Origin, access-control-allow-origin");

      if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
         response.setStatus(HttpServletResponse.SC_OK);
      } else {
         chain.doFilter(req, resp);
      }
   }

   @Override
   public void destroy() {
   }

}
