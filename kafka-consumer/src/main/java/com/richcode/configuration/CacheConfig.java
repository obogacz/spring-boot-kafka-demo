package com.richcode.configuration;

import com.richcode.domain.PurchaseEvent;
import org.infinispan.Cache;
import org.infinispan.commons.marshall.JavaSerializationMarshaller;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Configuration
class CacheConfig {

    private static final String PURCHASE_EVENT_CACHE = "purchaseEventCache";
    private static final long PURCHASE_EVENT_CACHE_EXPIRATION_TIME_IN_DAYS = 1;

    private static final Class[] CACHE_SERIALIZABLE_CLASSES = {
        UUID.class,
        PurchaseEvent.class
    };

    @Bean
    public Cache<UUID, PurchaseEvent> purchaseEventCache() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

        configurationBuilder
            .clustering()
            .cacheMode(CacheMode.REPL_SYNC);

        configurationBuilder
            .expiration()
            .maxIdle(PURCHASE_EVENT_CACHE_EXPIRATION_TIME_IN_DAYS, TimeUnit.DAYS);

        EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConfiguration());
        cacheManager.defineConfiguration(PURCHASE_EVENT_CACHE, configurationBuilder.build());
        return cacheManager.getCache(PURCHASE_EVENT_CACHE);
    }

    private GlobalConfiguration globalConfiguration() {
        GlobalConfigurationBuilder configurationBuilder = new GlobalConfigurationBuilder();

        configurationBuilder
            .transport()
            .defaultTransport()
            .clusterName("kafka-consumer-demo-cache");

        configurationBuilder
            .serialization()
            .marshaller(new JavaSerializationMarshaller())
            .allowList()
            .addClasses(CACHE_SERIALIZABLE_CLASSES);

        return configurationBuilder.build();
    }

}
