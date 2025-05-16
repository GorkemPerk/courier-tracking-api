package org.migros.one.couriertrackingapi.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum CalculationType {
    UNDEFINED,
    HAVERSINE,
    COSINES;

    @JsonCreator
    public static CalculationType parseType(String value) {
        return Arrays
                .stream(values())
                .filter(item -> item.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(UNDEFINED);
    }
}
