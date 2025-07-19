package com.frauddetection.service;

import com.frauddetection.dto.TransactionRequest;
import com.frauddetection.entity.TransactionEntity;
import com.frauddetection.entity.AlertEntity;
import com.frauddetection.repository.TransactionRepository;
import com.frauddetection.repository.AlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionService {
  private final TransactionRepository transactionRepository;
  private final AlertRepository alertRepository;

  public TransactionService(TransactionRepository transactionRepository, AlertRepository alertRepository) {
    this.transactionRepository = transactionRepository;
    this.alertRepository = alertRepository;
  }

  @Transactional
  public void saveTransaction(TransactionRequest request, boolean flagged, Double riskScore) {
    TransactionEntity entity = new TransactionEntity();
    entity.setTransactionId(request.getTransactionId());
    entity.setTransactionTime(request.getTransactionTime());
    entity.setAmount(request.getAmount());
    entity.setCurrency(request.getCurrency());
    entity.setStatus(flagged ? "FLAGGED" : "SUCCESS");
    entity.setRiskScore(riskScore);
    transactionRepository.save(entity);
    if (flagged) {
      AlertEntity alert = new AlertEntity();
      alert.setTransactionId(request.getTransactionId());
      alert.setAlertTime(LocalDateTime.now());
      alert.setReason("Flagged by rule engine");
      alertRepository.save(alert);
    }
  }
}
