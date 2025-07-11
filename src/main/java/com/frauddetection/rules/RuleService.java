
package com.frauddetection.rules;

import com.frauddetection.dto.TransactionRequest;
import com.frauddetection.entity.RuleEntity;
import com.frauddetection.repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RuleService {

  @Autowired
  private RuleRepository ruleRepository;

  private final ExpressionParser parser = new SpelExpressionParser();

  public String addOrUpdateRule(String name, String expression) {
    // Validate SpEL expression before adding
    try {
      parser.parseExpression(expression);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid Spring Expression Language (SpEL) expression: " + e.getMessage(), e);
    }
    // Check if a rule with the same name exists (update scenario)
    Optional<RuleEntity> existingOpt = ruleRepository.findAll().stream()
        .filter(r -> r.getName().equals(name))
        .findFirst();
    String id = existingOpt.map(RuleEntity::getId).orElse(UUID.randomUUID().toString());
    boolean wasActive = existingOpt.map(RuleEntity::isActive).orElse(false);

    RuleEntity ruleEntity = new RuleEntity();
    ruleEntity.setId(id);
    ruleEntity.setName(name);
    ruleEntity.setExpression(expression);
    ruleEntity.setActive(wasActive);
    ruleRepository.save(ruleEntity);
    return id;
  }

  @Transactional
  public void activateAllRules() {
    List<RuleEntity> all = ruleRepository.findAll();
    for (RuleEntity rule : all) {
      rule.setActive(true);
    }
    ruleRepository.saveAll(all);
  }

  @Transactional
  public void activateRule(String ruleId) {
    RuleEntity rule = ruleRepository.findById(ruleId)
        .orElseThrow(() -> new IllegalArgumentException("Rule not found: " + ruleId));
    rule.setActive(true);
    ruleRepository.save(rule);
  }

  @Transactional
  public void deactivateRule(String ruleId) {
    RuleEntity rule = ruleRepository.findById(ruleId)
        .orElseThrow(() -> new IllegalArgumentException("Rule not found: " + ruleId));
    rule.setActive(false);
    ruleRepository.save(rule);
  }

  public List<Rule> getAllRules() {
    return ruleRepository.findAll().stream()
        .map(this::toRule)
        .collect(Collectors.toList());
  }

  public List<Rule> getActiveRules() {
    return ruleRepository.findAll().stream()
        .filter(RuleEntity::isActive)
        .map(this::toRule)
        .collect(Collectors.toList());
  }

  @Transactional
  public boolean deleteRuleIfInactive(String ruleId) {
    Optional<RuleEntity> ruleOpt = ruleRepository.findById(ruleId);
    if (ruleOpt.isPresent() && !ruleOpt.get().isActive()) {
      ruleRepository.deleteById(ruleId);
      return true;
    }
    return false;
  }

  public boolean evaluate(TransactionRequest tx) {
    StandardEvaluationContext context = new StandardEvaluationContext(tx);
    for (Rule rule : getActiveRules()) {
      Expression exp = parser.parseExpression(rule.getExpression());
      Boolean result = exp.getValue(context, Boolean.class);
      if (Boolean.TRUE.equals(result)) {
        return true; // At least one rule matched
      }
    }
    return false;
  }

  private Rule toRule(RuleEntity entity) {
    return new Rule(entity.getId(), entity.getName(), entity.getExpression(), entity.isActive());
  }
}
