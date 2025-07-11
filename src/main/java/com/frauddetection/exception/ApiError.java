package com.frauddetection.exception;

import java.util.List;

public class ApiError {
  private String message;
  private List<FieldError> fieldErrors;

  public ApiError(String message, List<FieldError> fieldErrors) {
    this.message = message;
    this.fieldErrors = fieldErrors;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<FieldError> getFieldErrors() {
    return fieldErrors;
  }

  public void setFieldErrors(List<FieldError> fieldErrors) {
    this.fieldErrors = fieldErrors;
  }

  public static class FieldError {
    private String field;
    private String error;
    private String expected;

    public FieldError(String field, String error, String expected) {
      this.field = field;
      this.error = error;
      this.expected = expected;
    }

    public String getField() {
      return field;
    }

    public String getError() {
      return error;
    }

    public String getExpected() {
      return expected;
    }
  }
}
