package com.frauddetection.controller;

import com.frauddetection.rules.RuleService;
import com.frauddetection.rules.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rules")
public class RuleController {
  @Autowired
  private RuleService ruleService;

  @PostMapping
  public ResponseEntity<Map<String, String>> addRule(@RequestBody Map<String, String> body) {
    String name = body.get("name");
    String expression = body.get("expression");
    String ruleId = ruleService.addOrUpdateRule(name, expression);
    return ResponseEntity.ok(Map.of("ruleId", ruleId));
  }

  @PostMapping("/activateAll")
  public ResponseEntity<String> activateAllRules() {
    ruleService.activateAllRules();
    return ResponseEntity.ok("All rules activated");
  }

  @PostMapping("/activate/{id}")
  public ResponseEntity<String> activateRule(@PathVariable String id) {
    ruleService.activateRule(id);
    return ResponseEntity.ok("Rule activated");
  }

  @GetMapping("/active")
  public List<Rule> getActiveRules() {
    return ruleService.getActiveRules();
  }

  @GetMapping("/all")
  public List<Rule> getAllRules() {
    return ruleService.getAllRules();
  }

  @PostMapping("/deactivate/{id}")
  public ResponseEntity<String> deactivateRule(@PathVariable String id) {
    ruleService.deactivateRule(id);
    return ResponseEntity.ok("Rule deactivated");
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteRuleIfInactive(@PathVariable String id) {
    boolean deleted = ruleService.deleteRuleIfInactive(id);
    if (deleted) {
      return ResponseEntity.ok("Rule deleted");
    } else {
      return ResponseEntity.badRequest().body("Rule is active or does not exist");
    }
  }
}
