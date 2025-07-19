package com.frauddetection.service;

import com.frauddetection.dto.TransactionRequest;
import com.frauddetection.entity.TransactionEntity;
import com.frauddetection.entity.AlertEntity;
import com.frauddetection.repository.TransactionRepository;
import com.frauddetection.repository.AlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
  @Mock
  private TransactionRepository transactionRepository;
  @Mock
  private AlertRepository alertRepository;
  @InjectMocks
  private TransactionService transactionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSaveTransactionWithRiskScore() {
    TransactionRequest req = new TransactionRequest();
    req.setTransactionId("txn-1");
    req.setTransactionTime(LocalDateTime.now());
    req.setAmount(123.45);
    req.setCurrency("USD");
    double riskScore = 0.77;
    transactionService.saveTransaction(req, true, riskScore);
    ArgumentCaptor<TransactionEntity> captor = ArgumentCaptor.forClass(TransactionEntity.class);
    verify(transactionRepository).save(captor.capture());
    TransactionEntity entity = captor.getValue();
    assertEquals(riskScore, entity.getRiskScore());
    assertEquals("FLAGGED", entity.getStatus());
  }

  @Test
  void testSaveTransactionNoAlertIfNotFlagged() {
    TransactionRequest req = new TransactionRequest();
    req.setTransactionId("txn-2");
    req.setTransactionTime(LocalDateTime.now());
    req.setAmount(10.0);
    req.setCurrency("USD");
    transactionService.saveTransaction(req, false, 0.1);
    verify(alertRepository, never()).save(any(AlertEntity.class));
  }
}
