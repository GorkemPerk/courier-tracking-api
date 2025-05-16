package org.migros.one.couriertrackingapi.application.strategy;

import org.migros.one.couriertrackingapi.domain.model.enums.CalculationType;
import org.migros.one.couriertrackingapi.domain.model.vo.DistanceCalculationVO;

public interface DistanceCalculationStrategy {
    Double execute(DistanceCalculationVO distanceCalculation);

    boolean accept(CalculationType calculationType);
}
