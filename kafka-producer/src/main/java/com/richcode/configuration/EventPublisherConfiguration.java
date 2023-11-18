package com.richcode.configuration;

import com.richcode.domain.PurchaseEvent;
import com.richcode.publisher.AsyncEventPublisher;
import com.richcode.publisher.EventPublisher;
import com.richcode.publisher.SyncEventPublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import static com.richcode.StrategyConfiguration.AsyncPublishingStrategy;
import static com.richcode.StrategyConfiguration.SyncPublishingStrategy;

@Configuration
class EventPublisherConfiguration {

    @Bean
    @ConditionalOnBean(AsyncPublishingStrategy.class)
    public EventPublisher asyncEventPublisher(final KafkaProperties kafkaProperties,
                                              final KafkaTemplate<String, PurchaseEvent> kafkaTemplate) {
        return new AsyncEventPublisher(kafkaProperties, kafkaTemplate);
    }

    @Bean
    @ConditionalOnBean(SyncPublishingStrategy.class)
    public EventPublisher syncEventPublisher(final KafkaProperties kafkaProperties,
                                             final KafkaTemplate<String, PurchaseEvent> kafkaTemplate) {
        return new SyncEventPublisher(kafkaProperties, kafkaTemplate);
    }

}
