package com.sailing.es.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EsFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsFeignApplication.class, args);
    }

}
