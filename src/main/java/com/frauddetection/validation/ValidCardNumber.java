package com.frauddetection.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CardNumberValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCardNumber {
  String message() default "Invalid card number. Must be 13-19 digits.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
