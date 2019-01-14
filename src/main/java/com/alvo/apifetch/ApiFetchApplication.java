package com.alvo.apifetch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
@ComponentScan(basePackages = "com.meteogroup.apifetch")
public class ApiFetchApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiFetchApplication.class, args);
  }
}
