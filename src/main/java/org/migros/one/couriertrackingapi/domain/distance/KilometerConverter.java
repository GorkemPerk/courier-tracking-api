package org.migros.one.couriertrackingapi.domain.distance;

public class KilometerConverter implements DistanceUnitConverter {
    @Override
    public Double convert(Double distance) {
        return distance / 1000;
    }

    @Override
    public String getUnit() {
        return "km";
    }
}
