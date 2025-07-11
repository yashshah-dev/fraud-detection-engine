package com.frauddetection.validation;

public class CardNumberValidationStrategy implements ValidationStrategy {
  @Override
  public boolean isValid(String value) {
    if (value == null || value.isEmpty())
      return true;
    return value.matches("^\\d{13,19}$");
  }

  @Override
  public String getErrorMessage() {
    return "Invalid card number. Must be 13-19 digits.";
  }
}
