package org.migros.one.couriertrackingapi.application.strategy;

import lombok.RequiredArgsConstructor;
import org.migros.one.couriertrackingapi.domain.model.vo.DistanceCalculationVO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DistanceCalculationStrategyService {
    private final List<DistanceCalculationStrategy> calculationStrategies;

    public Double calculation(DistanceCalculationVO distanceCalculation) {
        return calculationStrategies.stream()
                .filter(strategy -> strategy.accept(distanceCalculation.getCalculationType()))
                .mapToDouble(strategy -> strategy.execute(distanceCalculation))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("distance calculation strategies found."));
    }
}
