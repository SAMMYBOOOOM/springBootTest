/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/10/2025 5:55 PM
 */
package com.example.springboottest.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    // Maximum time for cross-domain requests
    private static final long MAX_AGE = 24 * 60 * 60;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");    // Setting allowed origins
        corsConfiguration.addAllowedHeader("*");    // Setting allowed headers
        corsConfiguration.addAllowedMethod("*");    // Setting allowed methods
        corsConfiguration.setMaxAge(MAX_AGE);
        source.registerCorsConfiguration("/**", corsConfiguration); // Cross-domain configuration
        return new CorsFilter(source);
    }
}
