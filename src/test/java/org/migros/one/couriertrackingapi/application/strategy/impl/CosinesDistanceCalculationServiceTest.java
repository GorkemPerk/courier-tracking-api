package org.migros.one.couriertrackingapi.application.strategy.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.migros.one.couriertrackingapi.domain.model.enums.CalculationType;
import org.migros.one.couriertrackingapi.domain.model.vo.DistanceCalculationVO;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CosinesDistanceCalculationServiceTest {
    @InjectMocks
    private CosinesDistanceCalculationService sut;

    @Test
    void execute_shouldReturnCorrectDistance() {
        DistanceCalculationVO vo = DistanceCalculationVO.builder()
                .firstLatitude(41.0)
                .firstLongitude(29.0)
                .secondLatitude(41.1)
                .secondLongitude(29.1)
                .earthRadius(6371000.0)
                .build();

        Double result = sut.execute(vo);

        assertNotNull(result);
        assertTrue(result > 0);
        assertEquals(13927.011365114127, result, 500.0);
    }

    @Test
    void accept_shouldReturnTrueForCosinesType() {
        assertTrue(sut.accept(CalculationType.COSINES));
    }

    @Test
    void accept_shouldReturnFalseForOtherTypes() {
        assertFalse(sut.accept(CalculationType.HAVERSINE));
    }
}