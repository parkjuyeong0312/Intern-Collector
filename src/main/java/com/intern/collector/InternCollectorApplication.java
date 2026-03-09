package com.intern.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InternCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(InternCollectorApplication.class, args);
    }
}
