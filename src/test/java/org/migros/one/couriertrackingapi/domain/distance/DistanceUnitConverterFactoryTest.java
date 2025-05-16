package org.migros.one.couriertrackingapi.domain.distance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class DistanceUnitConverterFactoryTest {

    @ParameterizedTest()
    @CsvSource({"m", "meters", "metres"})
    void getConverter_shouldReturnMeterConverter_forMeterInputs(String input) {
        DistanceUnitConverter converter = DistanceUnitConverterFactory.getConverter(input);
        assertInstanceOf(MeterConverter.class, converter);
    }

    @ParameterizedTest()
    @CsvSource({"km", "kilometers", "kilometres"})
    void getConverter_shouldReturnKilometerConverter_forKilometerInputs(String input) {
        DistanceUnitConverter converter = DistanceUnitConverterFactory.getConverter(input);
        assertInstanceOf(KilometerConverter.class, converter);
    }

    @Test
    void getConverter_shouldThrowException_forUnsupportedUnit() {
        String invalidUnit = "mile";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            DistanceUnitConverterFactory.getConverter(invalidUnit);
        });

        assertEquals("Unsupported distance unit: " + invalidUnit, exception.getMessage());
    }
}