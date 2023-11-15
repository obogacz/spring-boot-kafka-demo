package com.richcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ConsumerApp {
    public static void main( String[] args ) {
        SpringApplication.run(ConsumerApp.class, args);
    }
}
