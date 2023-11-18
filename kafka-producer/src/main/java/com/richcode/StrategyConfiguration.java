package com.richcode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@Slf4j
public class StrategyConfiguration {

    private static final String KAFKA_PRODUCER_PUBLISHING_PROPERTY = "kafka.producer.publishing";
    private static final String KAFKA_PRODUCER_PUBLISHING_SYNC_VALUE = "sync";
    private static final String KAFKA_PRODUCER_PUBLISHING_ASYNC_VALUE = "async";

    private static final String KAFKA_PRODUCER_STRATEGY_PROPERTY = "kafka.producer.strategy";
    private static final String KAFKA_PRODUCER_STRATEGY_AT_MOST_ONCE_VALUE = "at-most-once";
    private static final String KAFKA_PRODUCER_STRATEGY_AT_LEAST_ONCE_VALUE = "at-least-once";
    private static final String KAFKA_PRODUCER_STRATEGY_EXACTLY_ONCE_VALUE = "exactly-once";

    public static class AtMostOnceProducerStrategy{}
    public static class AtLeastOnceProducerStrategy{}
    public static class ExactlyOnceProducerStrategy{}
    public static class SyncPublishingStrategy{}
    public static class AsyncPublishingStrategy{}

    @Bean
    @ConditionalOnProperty(
        name = KAFKA_PRODUCER_STRATEGY_PROPERTY,
        havingValue = KAFKA_PRODUCER_STRATEGY_AT_MOST_ONCE_VALUE,
        matchIfMissing = true)
    public AtMostOnceProducerStrategy atMostOnceProducerStrategy() {
        log.info("Configured at-most-once publishing strategy");
        return new AtMostOnceProducerStrategy();
    }

    @Bean
    @ConditionalOnProperty(
        name = KAFKA_PRODUCER_STRATEGY_PROPERTY,
        havingValue = KAFKA_PRODUCER_STRATEGY_AT_LEAST_ONCE_VALUE)
    public AtLeastOnceProducerStrategy atLeastOnceProducerStrategy() {
        log.info("Configured at-least-once publishing strategy");
        return new AtLeastOnceProducerStrategy();
    }

    @Bean
    @ConditionalOnProperty(
        name = KAFKA_PRODUCER_STRATEGY_PROPERTY,
        havingValue = KAFKA_PRODUCER_STRATEGY_EXACTLY_ONCE_VALUE)
    public ExactlyOnceProducerStrategy exactlyOnceProducerStrategy() {
        log.info("Configured exactly-once publishing strategy");
        return new ExactlyOnceProducerStrategy();
    }

    @Bean
    @ConditionalOnProperty(
        name = KAFKA_PRODUCER_PUBLISHING_PROPERTY,
        havingValue = KAFKA_PRODUCER_PUBLISHING_SYNC_VALUE)
    public SyncPublishingStrategy syncPublishingStrategy() {
        log.info("Configured synchronous publishing strategy");
        return new SyncPublishingStrategy();
    }

    @Bean
    @ConditionalOnProperty(
        name = KAFKA_PRODUCER_PUBLISHING_PROPERTY,
        havingValue = KAFKA_PRODUCER_PUBLISHING_ASYNC_VALUE)
    public AsyncPublishingStrategy asyncPublishingStrategy() {
        log.info("Configured asynchronous publishing strategy");
        return new AsyncPublishingStrategy();
    }

}
