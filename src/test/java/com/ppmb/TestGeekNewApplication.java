package com.ppmb;

import org.springframework.boot.SpringApplication;

public class TestGeekNewApplication {

    public static void main(String[] args) {
        SpringApplication.from(GeekNewApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
