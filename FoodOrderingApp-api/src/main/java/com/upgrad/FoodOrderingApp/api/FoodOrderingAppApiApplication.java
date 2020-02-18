package com.upgrad.FoodOrderingApp.api;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.upgrad.FoodOrderingApp.service.ServiceConfiguration;

/**
 * A Configuration class that can declare one or more @Bean methods and trigger auto-configuration and component scanning.
 * This class launches a Spring Application from Java main method.
 */
@SpringBootApplication
@ComponentScan ({"com.upgrad.FoodOrderingApp", "com.upgrad.FoodOrderingApp.api.config"})
@EnableJpaRepositories("com.upgrad.FoodOrderingApp.service.dao")
@EntityScan("com.upgrad.FoodOrderingApp.service.entity")
@Import(ServiceConfiguration.class)
public class FoodOrderingAppApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodOrderingAppApiApplication.class, args);
    }
}

