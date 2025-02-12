/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/11/2025 9:18 PM
 */
package com.example.springboottest.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(jwtInterceptor()).addPathPatterns("/**")   // Setting jwt rule and intercept path
                .excludePathPatterns("/login"); // Exclude register at AuthAccess
        super.addInterceptors(registry);
    }

    @Bean
    public JwtInterceptor jwtInterceptor(){
        return new JwtInterceptor();
    }
}
