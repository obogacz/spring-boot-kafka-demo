package com.richcode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@Slf4j
public class StrategyConfiguration {

    private static final String KAFKA_CONSUMER_TYPE_PROPERTY = "kafka.consumer.type";
    private static final String KAFKA_CONSUMER_TYPE_SINGLE_VALUE = "single";
    private static final String KAFKA_CONSUMER_TYPE_BATCH_VALUE = "batch";

    private static final String KAFKA_CONSUMER_STRATEGY_PROPERTY = "kafka.consumer.strategy";
    private static final String KAFKA_CONSUMER_STRATEGY_AT_MOST_ONCE_VALUE = "at-most-once";
    private static final String KAFKA_CONSUMER_STRATEGY_AT_LEAST_ONCE_VALUE = "at-least-once";
    private static final String KAFKA_CONSUMER_STRATEGY_EXACTLY_ONCE_VALUE = "exactly-once";

    public static class AtMostOnceConsumerStrategy{}
    public static class AtLeastOnceConsumerStrategy{}
    public static class ExactlyOnceConsumerStrategy{}
    public static class SingleEventListenerStrategy{}
    public static class BatchEventListenerStrategy{}

    @Bean
    @ConditionalOnProperty(
        name = KAFKA_CONSUMER_STRATEGY_PROPERTY,
        havingValue = KAFKA_CONSUMER_STRATEGY_AT_MOST_ONCE_VALUE,
        matchIfMissing = true)
    public AtMostOnceConsumerStrategy atMostOnceConsumerStrategy() {
        log.info("Configured at-most-once consuming strategy");
        return new AtMostOnceConsumerStrategy();
    }

    @Bean
    @ConditionalOnProperty(
        name = KAFKA_CONSUMER_STRATEGY_PROPERTY,
        havingValue = KAFKA_CONSUMER_STRATEGY_AT_LEAST_ONCE_VALUE)
    public AtLeastOnceConsumerStrategy atLeastOnceConsumerStrategy() {
        log.info("Configured at-least-once consuming strategy");
        return new AtLeastOnceConsumerStrategy();
    }

    @Bean
    @ConditionalOnProperty(
        name = KAFKA_CONSUMER_STRATEGY_PROPERTY,
        havingValue = KAFKA_CONSUMER_STRATEGY_EXACTLY_ONCE_VALUE)
    public ExactlyOnceConsumerStrategy exactlyOnceConsumerStrategy() {
        log.info("Configured exactly-once consuming strategy");
        return new ExactlyOnceConsumerStrategy();
    }

    @Bean
    @ConditionalOnProperty(
        name = KAFKA_CONSUMER_TYPE_PROPERTY,
        havingValue = KAFKA_CONSUMER_TYPE_SINGLE_VALUE,
        matchIfMissing = true)
    public SingleEventListenerStrategy singleEventListenerStrategy() {
        log.info("Configured single event consumer");
        return new SingleEventListenerStrategy();
    }

    @Bean
    @ConditionalOnProperty(
        name = KAFKA_CONSUMER_TYPE_PROPERTY,
        havingValue = KAFKA_CONSUMER_TYPE_BATCH_VALUE)
    public BatchEventListenerStrategy batchEventListenerStrategy() {
        log.info("Configured batch event consumer");
        return new BatchEventListenerStrategy();
    }

}
