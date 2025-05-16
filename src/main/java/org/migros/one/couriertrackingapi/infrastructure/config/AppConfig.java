package org.migros.one.couriertrackingapi.infrastructure.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@RequiredArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private Long maxThreshold;
    private Double earthRadius;
    private Long periodThreshold;
    private String distanceUnit;
}
