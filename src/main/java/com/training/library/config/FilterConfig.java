package com.training.library.config;

import com.training.library.filter.LoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<LoggingFilter> customLoggingFilter(){
        FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new LoggingFilter());
        registrationBean.addUrlPatterns("/*"); // Apply filter to all URLs
        registrationBean.setOrder(1); // Set precedence if there are multiple filters

        return registrationBean;
    }
}