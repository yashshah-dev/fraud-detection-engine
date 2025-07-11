package com.frauddetection.validation;

import java.util.regex.Pattern;

public class IPAddressValidationStrategy implements ValidationStrategy {
  private static final Pattern IPV4_PATTERN = Pattern.compile(
      "^(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}$");
  private static final Pattern IPV6_PATTERN = Pattern.compile(
      "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

  @Override
  public boolean isValid(String value) {
    if (value == null || value.isEmpty())
      return true;
    return IPV4_PATTERN.matcher(value).matches() || IPV6_PATTERN.matcher(value).matches();
  }

  @Override
  public String getErrorMessage() {
    return "Invalid IP address format.";
  }
}
