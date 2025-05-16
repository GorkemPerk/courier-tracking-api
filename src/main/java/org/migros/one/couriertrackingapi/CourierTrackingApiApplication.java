package org.migros.one.couriertrackingapi;

import org.migros.one.couriertrackingapi.infrastructure.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EnableJpaAuditing
@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
@EnableCaching
public class CourierTrackingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourierTrackingApiApplication.class, args);
    }

}
