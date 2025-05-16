package org.migros.one.couriertrackingapi.application.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.migros.one.couriertrackingapi.domain.model.enums.CalculationType;
import org.migros.one.couriertrackingapi.domain.model.vo.DistanceCalculationVO;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DistanceCalculationStrategyServiceTest {
    @Mock
    private DistanceCalculationStrategy distanceCalculationStrategy;

    private DistanceCalculationStrategyService sut;

    @BeforeEach
    void setUp() {
        sut = new DistanceCalculationStrategyService(List.of(distanceCalculationStrategy));
    }

    @Test
    void calculation_shouldUseCorrectStrategyAndReturnDistance() {
        DistanceCalculationVO distanceCalculation = DistanceCalculationVO.builder()
                .firstLatitude(41.0)
                .firstLongitude(29.0)
                .secondLatitude(41.1)
                .secondLongitude(29.1)
                .earthRadius(6371.0)
                .calculationType(CalculationType.HAVERSINE)
                .build();

        when(distanceCalculationStrategy.accept(CalculationType.HAVERSINE)).thenReturn(true);
        when(distanceCalculationStrategy.execute(distanceCalculation)).thenReturn(100.0);

        Double result = sut.calculation(distanceCalculation);

        assertEquals(100.0, result);
        verify(distanceCalculationStrategy).accept(CalculationType.HAVERSINE);
        verify(distanceCalculationStrategy).execute(distanceCalculation);
    }

    @Test
    void calculation_shouldThrowExceptionWhenNoStrategyFound() {
        DistanceCalculationVO distanceCalculation = DistanceCalculationVO.builder()
                .firstLatitude(41.0)
                .firstLongitude(29.0)
                .secondLatitude(41.1)
                .secondLongitude(29.1)
                .earthRadius(6371.0)
                .calculationType(CalculationType.HAVERSINE)
                .build();

        when(distanceCalculationStrategy.accept(CalculationType.HAVERSINE)).thenReturn(false);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> sut.calculation(distanceCalculation));

        assertEquals("distance calculation strategies found.", exception.getMessage());
        verify(distanceCalculationStrategy).accept(CalculationType.HAVERSINE);
        verify(distanceCalculationStrategy, never()).execute(any());
    }
}