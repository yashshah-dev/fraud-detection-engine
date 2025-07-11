package com.frauddetection.config;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AccessLogFilter implements Filter {
  private static final Logger logger = LoggerFactory.getLogger(AccessLogFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    Span currentSpan = Span.current();
    SpanContext ctx = currentSpan.getSpanContext();
    logger.info("AccessLog: method={}, uri={}, traceId={}, spanId={}",
        httpRequest.getMethod(),
        httpRequest.getRequestURI(),
        ctx.isValid() ? ctx.getTraceId() : "",
        ctx.isValid() ? ctx.getSpanId() : "");
    chain.doFilter(request, response);
  }
}
