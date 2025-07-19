package com.frauddetection.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.*;
import com.frauddetection.validation.ValidCardNumber;
import com.frauddetection.validation.ValidIPAddress;

public class TransactionRequest {
  @NotNull(message = "transactionTime is required. Format: yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime transactionTime;
  @NotBlank(message = "transactionId is required.")
  private String transactionId;
  @Positive(message = "amount must be positive.")
  private double amount;
  @NotBlank(message = "currency is required. Format: 3-letter ISO code (e.g. USD)")
  @Pattern(regexp = "^[A-Z]{3}$", message = "currency must be a 3-letter ISO code (e.g. USD)")
  private String currency;
  @NotBlank(message = "accountId is required.")
  private String accountId;
  @NotBlank(message = "transactionType is required. Allowed: POS, ONLINE")
  @Pattern(regexp = "POS|ONLINE", message = "transactionType must be POS or ONLINE")
  private String transactionType; // POS or ONLINE
  @ValidCardNumber
  private String cardNumber;
  private String merchantId;
  private String merchantLocation;
  private String deviceId; // For POS
  @ValidIPAddress
  private String ipAddress; // For ONLINE

  // Payor details
  private String payorAccountId;
  private String payorName;
  private String payorId;

  // Payee details
  private String payeeAccountId;
  private String payeeName;
  private String payeeId;

  public String getPayorAccountId() {
    return payorAccountId;
  }

  public void setPayorAccountId(String payorAccountId) {
    this.payorAccountId = payorAccountId;
  }

  public String getPayorName() {
    return payorName;
  }

  public void setPayorName(String payorName) {
    this.payorName = payorName;
  }

  public String getPayorId() {
    return payorId;
  }

  public void setPayorId(String payorId) {
    this.payorId = payorId;
  }

  public String getPayeeAccountId() {
    return payeeAccountId;
  }

  public void setPayeeAccountId(String payeeAccountId) {
    this.payeeAccountId = payeeAccountId;
  }

  public String getPayeeName() {
    return payeeName;
  }

  public void setPayeeName(String payeeName) {
    this.payeeName = payeeName;
  }

  public String getPayeeId() {
    return payeeId;
  }

  public void setPayeeId(String payeeId) {
    this.payeeId = payeeId;
  }

  // Getters and setters
  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public String getMerchantId() {
    return merchantId;
  }

  public void setMerchantId(String merchantId) {
    this.merchantId = merchantId;
  }

  public String getMerchantLocation() {
    return merchantLocation;
  }

  public void setMerchantLocation(String merchantLocation) {
    this.merchantLocation = merchantLocation;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public LocalDateTime getTransactionTime() {
    return transactionTime;
  }

  public void setTransactionTime(LocalDateTime transactionTime) {
    this.transactionTime = transactionTime;
  }

  // For SpEL rule context: allow riskScore as a pseudo-property
  private Double riskScore;

  public Double getRiskScore() {
    return riskScore;
  }

  public void setRiskScore(Double riskScore) {
    this.riskScore = riskScore;
  }
}