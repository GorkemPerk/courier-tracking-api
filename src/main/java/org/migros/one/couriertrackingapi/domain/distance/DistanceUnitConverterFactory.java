package org.migros.one.couriertrackingapi.domain.distance;

public class DistanceUnitConverterFactory {
    public static DistanceUnitConverter getConverter(String unit) {
        return switch (unit.toLowerCase()) {
            case "m", "meters", "metres" -> new MeterConverter();
            case "km", "kilometers", "kilometres" -> new KilometerConverter();
            default -> throw new IllegalArgumentException("Unsupported distance unit: " + unit);
        };
    }
}
