package com.richcode.configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
class KafkaTopicConfiguration {

    @Bean
    public KafkaAdmin kafkaAdmin(final KafkaProperties kafkaProperties) {
        final Map<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapAddress());
        return new KafkaAdmin(config);
    }

    @Bean
    public NewTopic topicPurchases(final KafkaProperties kafkaProperties) {
        return new NewTopic(
            kafkaProperties.getTopicPurchasesName(),
            kafkaProperties.getTopicPurchasesPartitions(),
            kafkaProperties.getTopicPurchasesReplicas());
    }

}
