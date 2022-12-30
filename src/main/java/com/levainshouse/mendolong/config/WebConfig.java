package com.levainshouse.mendolong.config;

import com.levainshouse.mendolong.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile(value = "prod")
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/actuator/health")
                .allowedOrigins("*")
                .allowedMethods("GET")
                .allowedHeaders("*")
                .maxAge(3000);

        registry.addMapping("/docs/index.html")
                .allowedOrigins("*")
                .allowedMethods("GET")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .maxAge(3000);

        registry.addMapping("/**")
                .allowedOrigins("https://levains.vercel.app")
                .allowedOriginPatterns("https://levains.vercel.app")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3000);
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico");
    }
}
