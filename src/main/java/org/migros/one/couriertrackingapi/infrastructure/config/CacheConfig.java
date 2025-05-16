package org.migros.one.couriertrackingapi.infrastructure.config;

import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {
    private static final String CACHE_NAME = "storeCache";

    @Bean
    public CaffeineCacheManager cacheManager() {
        return new CaffeineCacheManager(CACHE_NAME);
    }
}
