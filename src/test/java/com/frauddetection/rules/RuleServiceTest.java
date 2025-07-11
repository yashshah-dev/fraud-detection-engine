package com.frauddetection.rules;

import com.frauddetection.dto.TransactionRequest;
import com.frauddetection.entity.RuleEntity;
import com.frauddetection.repository.RuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.expression.spel.SpelParseException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RuleServiceTest {
  @Mock
  private RuleRepository ruleRepository;
  @InjectMocks
  private RuleService ruleService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testEvaluateWithRiskScoreContext() {
    RuleEntity ruleEntity = new RuleEntity();
    ruleEntity.setId("1");
    ruleEntity.setName("HighRisk");
    ruleEntity.setExpression("riskScore > 0.5");
    ruleEntity.setActive(true);
    when(ruleRepository.findAll()).thenReturn(List.of(ruleEntity));
    TransactionRequest tx = new TransactionRequest();
    tx.setRiskScore(0.7);
    boolean flagged = ruleService.evaluate(tx);
    assertTrue(flagged);
    tx.setRiskScore(0.2);
    flagged = ruleService.evaluate(tx);
    assertFalse(flagged);
  }

  @Test
  void testEvaluateWithInvalidSpEL() {
    RuleEntity ruleEntity = new RuleEntity();
    ruleEntity.setId("2");
    ruleEntity.setName("Invalid");
    ruleEntity.setExpression("amount > > 100");
    ruleEntity.setActive(true);
    when(ruleRepository.findAll()).thenReturn(List.of(ruleEntity));
    TransactionRequest tx = new TransactionRequest();
    tx.setRiskScore(0.1);
    assertThrows(SpelParseException.class, () -> ruleService.evaluate(tx));
  }
}
