package com.richcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProducerApp {

    private static final Class[] PRIMARY_CONFIG_SOURCES = { StrategyConfiguration.class, ProducerApp.class };

    public static void main( String[] args ) {
        SpringApplication.run(PRIMARY_CONFIG_SOURCES, args);
    }

}
