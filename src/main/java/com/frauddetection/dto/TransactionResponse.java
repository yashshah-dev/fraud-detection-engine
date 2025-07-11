package com.frauddetection.dto;

public class TransactionResponse {
    private String transactionId;
    private double riskScore;
    private String status;

    public TransactionResponse(String transactionId, double riskScore, String status) {
        this.transactionId = transactionId;
        this.riskScore = riskScore;
        this.status = status;
    }

    // Getters and setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public double getRiskScore() { return riskScore; }
    public void setRiskScore(double riskScore) { this.riskScore = riskScore; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
