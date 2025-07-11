package com.frauddetection.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IPAddressValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIPAddress {
  String message() default "Invalid IP address format.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
