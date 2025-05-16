package org.migros.one.couriertrackingapi.application.strategy.impl;

import org.migros.one.couriertrackingapi.application.strategy.DistanceCalculationStrategy;
import org.migros.one.couriertrackingapi.domain.model.enums.CalculationType;
import org.migros.one.couriertrackingapi.domain.model.vo.DistanceCalculationVO;
import org.springframework.stereotype.Component;

@Component
public class CosinesDistanceCalculationService implements DistanceCalculationStrategy {
    @Override
    public Double execute(DistanceCalculationVO distanceCalculation) {
        double lat1Rad = Math.toRadians(distanceCalculation.getFirstLatitude());
        double lat2Rad = Math.toRadians(distanceCalculation.getSecondLatitude());
        double deltaLon = Math.toRadians(distanceCalculation.getSecondLongitude() - distanceCalculation.getFirstLongitude());
        return Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(deltaLon))
                * distanceCalculation.getEarthRadius();
    }

    @Override
    public boolean accept(CalculationType calculationType) {
        return calculationType.equals(CalculationType.COSINES);
    }
}