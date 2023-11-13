package com.richcode.configuration;

import com.richcode.domain.PurchaseEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
class KafkaProducerConfiguration {

    @Bean
    public ProducerFactory<String, PurchaseEvent> producerFactory(final KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.getSafeSettings());
    }

    @Bean
    public KafkaTemplate<String, PurchaseEvent> kafkaTemplate(final KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(producerFactory(kafkaProperties));
    }

}
