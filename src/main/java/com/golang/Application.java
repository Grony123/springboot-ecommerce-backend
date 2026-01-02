package com.golang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "com.golang")
@EnableCaching
public class Application {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(Application.class, args);
    }
}