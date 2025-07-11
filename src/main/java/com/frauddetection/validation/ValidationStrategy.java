package com.frauddetection.validation;

public interface ValidationStrategy {
  boolean isValid(String value);

  String getErrorMessage();
}
