package com.frauddetection.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {
    List<ApiError.FieldError> errors = ex.getBindingResult().getFieldErrors().stream()
        .map(this::toFieldError)
        .collect(Collectors.toList());
    ApiError apiError = new ApiError("Validation failed", errors);
    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  private ApiError.FieldError toFieldError(FieldError fieldError) {
    String expected = "";
    if (fieldError.getCode() != null) {
      switch (fieldError.getCode()) {
        case "NotNull":
          expected = "not null";
          break;
        case "NotBlank":
          expected = "not blank";
          break;
        case "Pattern":
          expected = "pattern: " + fieldError.getRejectedValue();
          break;
        case "Positive":
          expected = "> 0";
          break;
        default:
          expected = fieldError.getDefaultMessage();
      }
    }
    return new ApiError.FieldError(fieldError.getField(), fieldError.getDefaultMessage(), expected);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
    ApiError apiError = new ApiError(ex.getMessage(), null);
    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }
}
