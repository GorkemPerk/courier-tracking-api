package org.migros.one.couriertrackingapi.presentation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class TrackingDateValidator implements ConstraintValidator<ValidTrackingDate, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) return false;

        LocalDateTime now = LocalDateTime.now().withNano(0);
        LocalDateTime maxTime = now.plusMinutes(30);
        return (!value.isBefore(now)) && (!value.isAfter(maxTime));
    }
}
