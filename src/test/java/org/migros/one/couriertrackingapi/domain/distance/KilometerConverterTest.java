package org.migros.one.couriertrackingapi.domain.distance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class KilometerConverterTest {
    @InjectMocks
    private KilometerConverter sut;

    @Test
    void convert_shouldDivideBy1000() {
        Double input = 1500.0;
        Double result = sut.convert(input);
        assertEquals(1.5, result);
    }

    @Test
    void getUnit_shouldReturnMetersSymbol() {
        String unit = sut.getUnit();
        assertEquals("km", unit);
    }

}