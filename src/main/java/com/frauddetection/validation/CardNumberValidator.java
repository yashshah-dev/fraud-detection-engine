package com.frauddetection.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CardNumberValidator implements ConstraintValidator<ValidCardNumber, String> {
  private final ValidationStrategy strategy = new CardNumberValidationStrategy();

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return strategy.isValid(value);
  }
}
