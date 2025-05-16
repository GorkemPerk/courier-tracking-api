package org.migros.one.couriertrackingapi.application.strategy.impl;

import org.migros.one.couriertrackingapi.application.strategy.DistanceCalculationStrategy;
import org.migros.one.couriertrackingapi.domain.model.enums.CalculationType;
import org.migros.one.couriertrackingapi.domain.model.vo.DistanceCalculationVO;
import org.springframework.stereotype.Component;

@Component
public class HaversineDistanceCalculationService implements DistanceCalculationStrategy {
    @Override
    public Double execute(DistanceCalculationVO distanceCalculation) {
        double latDistance = Math.toRadians(distanceCalculation.getFirstLatitude() - distanceCalculation.getSecondLatitude());
        double lonDistance = Math.toRadians(distanceCalculation.getSecondLatitude() - distanceCalculation.getFirstLatitude());

        double angularDistanceComponent = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(distanceCalculation.getSecondLatitude())) * Math.cos(Math.toRadians(distanceCalculation.getFirstLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double centralAngle = 2 * Math.atan2(Math.sqrt(angularDistanceComponent), Math.sqrt(1 - angularDistanceComponent));
        return distanceCalculation.getEarthRadius() * centralAngle;
    }

    @Override
    public boolean accept(CalculationType calculationType) {
        return calculationType.equals(CalculationType.HAVERSINE);
    }
}