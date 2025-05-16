package org.migros.one.couriertrackingapi.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum DistanceUnit {
    UNDEFINED(""),
    METERS("m"),
    KILOMETERS("km");

    private final String unit;

    DistanceUnit(String unit) {
        this.unit = unit;
    }

    @JsonCreator
    public static DistanceUnit parseType(String value) {
        return Arrays
                .stream(values())
                .filter(item -> item.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(UNDEFINED);
    }

}
