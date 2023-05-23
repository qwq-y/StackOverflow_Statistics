package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Java2ProjApplication {

  public static void main(String[] args) {
    SpringApplication.run(Java2ProjApplication.class, args);
  }
  @Configuration
  public class CrosConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/**")
              .allowedOriginPatterns("*")
              .allowedMethods("GET","HEAD","POST","DELETE","OPTIONS","PUT")
              .allowCredentials(true)
              .maxAge(3600)
              .allowedHeaders("*");
    }
  }
}
