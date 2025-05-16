package org.migros.one.couriertrackingapi.domain.distance;

public class MeterConverter implements DistanceUnitConverter {
    @Override
    public Double convert(Double distance) {
        return distance;
    }

    @Override
    public String getUnit() {
        return "m";
    }
}
