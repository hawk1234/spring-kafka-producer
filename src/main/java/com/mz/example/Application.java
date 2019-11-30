package com.mz.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//TODO:
// - no validation of incoming request is made
// - no tests for MVC
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
