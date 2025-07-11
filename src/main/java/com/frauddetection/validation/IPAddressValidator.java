package com.frauddetection.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IPAddressValidator implements ConstraintValidator<ValidIPAddress, String> {
  private final ValidationStrategy strategy = new IPAddressValidationStrategy();

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return strategy.isValid(value);
  }
}
