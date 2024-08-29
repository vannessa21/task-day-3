package com.training.library.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Record the start time
        long startTime = System.nanoTime();

        try {
            // Proceed with the next filter in the chain
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            // Record the end time
            long duration = System.nanoTime() - startTime;

            // Log the details
            logger.info("Accessed URL: {}, HTTP Method: {}, HTTP Status: {}, Time Taken: {} ms",
                    request.getRequestURI(),
                    request.getMethod(),
                    response.getStatus(),
                    duration / 1_000_000); // Convert to milliseconds
        }
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }

}