package com.frauddetection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RuleControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testAddRuleAndActivateById() throws Exception {
    // Add rule
    String ruleJson = objectMapper.writeValueAsString(Map.of(
        "name", "HighAmount",
        "expression", "amount > 1000"));
    String response = mockMvc.perform(post("/api/rules")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ruleJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ruleId").exists())
        .andReturn().getResponse().getContentAsString();
    String ruleId = objectMapper.readTree(response).get("ruleId").asText();

    // Activate rule by id
    mockMvc.perform(post("/api/rules/activate/" + ruleId))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("activated")));
  }

  @Test
  void testActivateAllRules() throws Exception {
    mockMvc.perform(post("/api/rules/activateAll"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("All rules activated")));
  }

  @Test
  void testGetAllRules() throws Exception {
    mockMvc.perform(get("/api/rules/all"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", isA(java.util.List.class)));
  }

  @Test
  void testDeactivateRuleById() throws Exception {
    // Add rule
    String ruleJson = objectMapper.writeValueAsString(Map.of(
        "name", "TestDeactivate",
        "expression", "amount > 500"));
    String response = mockMvc.perform(post("/api/rules")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ruleJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ruleId").exists())
        .andReturn().getResponse().getContentAsString();
    String ruleId = objectMapper.readTree(response).get("ruleId").asText();

    // Activate rule
    mockMvc.perform(post("/api/rules/activate/" + ruleId))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("activated")));

    // Deactivate rule
    mockMvc.perform(post("/api/rules/deactivate/" + ruleId))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("deactivated")));

    // Ensure rule is not in active rules
    mockMvc.perform(get("/api/rules/active"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.id=='" + ruleId + "')]").doesNotExist());
  }

  @Test
  void testUpdateRule() throws Exception {
    // Add rule
    String ruleJson = objectMapper.writeValueAsString(Map.of(
        "name", "ToUpdate",
        "expression", "amount > 10"));
    String response = mockMvc.perform(post("/api/rules")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ruleJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ruleId").exists())
        .andReturn().getResponse().getContentAsString();
    // String ruleId = objectMapper.readTree(response).get("ruleId").asText();

    // Update rule (same endpoint, same id, new expression)
    String updatedRuleJson = objectMapper.writeValueAsString(Map.of(
        "name", "ToUpdate",
        "expression", "amount > 20"));
    mockMvc.perform(post("/api/rules")
        .contentType(MediaType.APPLICATION_JSON)
        .content(updatedRuleJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ruleId").exists());
  }

  @Test
  void testDeleteInactiveRule() throws Exception {
    // Add rule
    String ruleJson = objectMapper.writeValueAsString(Map.of(
        "name", "ToDelete",
        "expression", "amount > 30"));
    String response = mockMvc.perform(post("/api/rules")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ruleJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ruleId").exists())
        .andReturn().getResponse().getContentAsString();
    String ruleId = objectMapper.readTree(response).get("ruleId").asText();

    // Delete inactive rule
    mockMvc.perform(delete("/api/rules/delete/" + ruleId))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("deleted")));
  }

  @Test
  void testDeleteActiveRuleShouldFail() throws Exception {
    // Add rule
    String ruleJson = objectMapper.writeValueAsString(Map.of(
        "name", "ActiveDeleteFail",
        "expression", "amount > 40"));
    String response = mockMvc.perform(post("/api/rules")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ruleJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ruleId").exists())
        .andReturn().getResponse().getContentAsString();
    String ruleId = objectMapper.readTree(response).get("ruleId").asText();

    // Activate rule
    mockMvc.perform(post("/api/rules/activate/" + ruleId))
        .andExpect(status().isOk());

    // Try to delete active rule
    mockMvc.perform(delete("/api/rules/delete/" + ruleId))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("active")));
  }

  @Test
  void testAddInvalidSpELRule() throws Exception {
    String ruleJson = objectMapper.writeValueAsString(Map.of(
        "name", "InvalidSpEL",
        "expression", "amount > > 100")); // Invalid SpEL
    mockMvc.perform(post("/api/rules")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ruleJson))
        .andExpect(status().isBadRequest());
  }
}
