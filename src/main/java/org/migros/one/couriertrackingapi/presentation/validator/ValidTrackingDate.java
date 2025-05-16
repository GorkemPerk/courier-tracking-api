package org.migros.one.couriertrackingapi.presentation.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TrackingDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTrackingDate {
    String message() default "Invalid tracking entry date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
