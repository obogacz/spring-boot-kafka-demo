package com.richcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ConsumerApp {

    private static final Class[] PRIMARY_CONFIG_SOURCES = { StrategyConfiguration.class, ConsumerApp.class };

    public static void main( String[] args ) {
        SpringApplication.run(PRIMARY_CONFIG_SOURCES, args);
    }
}
