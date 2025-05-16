package org.migros.one.couriertrackingapi.domain.distance;

public interface DistanceUnitConverter {
    Double convert(Double distance);

    String getUnit();
}
