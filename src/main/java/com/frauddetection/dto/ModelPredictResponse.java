package com.frauddetection.dto;

public class ModelPredictResponse {
  private double risk_score;

  public double getRisk_score() {
    return risk_score;
  }

  public void setRisk_score(double risk_score) {
    this.risk_score = risk_score;
  }
}
