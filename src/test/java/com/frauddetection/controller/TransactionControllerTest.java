package com.frauddetection.controller;

import com.frauddetection.dto.TransactionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.when;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {
  @MockBean
  private com.frauddetection.service.ModelPredictService modelPredictService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testProcessTransaction() throws Exception {
    TransactionRequest request = new TransactionRequest();
    request.setTransactionId("txn-123");
    request.setAmount(100.0);
    request.setCurrency("USD");
    request.setAccountId("acc-456");
    request.setTransactionType("POS");
    request.setTransactionTime(java.time.LocalDateTime.now());
    // Optionally set cardNumber, merchantId, etc. if needed for validation

    mockMvc.perform(post("/api/transaction")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.transactionId").value("txn-123"))
        .andExpect(jsonPath("$.riskScore").exists())
        // Accept either SUCCESS or FLAGGED depending on rules present
        .andExpect(jsonPath("$.status", anyOf(is("SUCCESS"), is("FLAGGED"))));
  }

  @Test
  void testProcessTransaction_MissingFields() throws Exception {
    TransactionRequest request = new TransactionRequest();
    request.setTransactionId("txn-missing");
    // Missing required fields like amount, currency, etc.
    mockMvc.perform(post("/api/transaction")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  // This test assumes the model predict API is down or returns error, so
  // riskScore should default to 0.0
  @Test
  void testProcessTransaction_ModelPredictApiFailure() throws Exception {
    TransactionRequest request = new TransactionRequest();
    request.setTransactionId("txn-api-fail");
    request.setAmount(100.0);
    request.setCurrency("USD");
    request.setAccountId("acc-789");
    request.setTransactionType("POS");
    request.setTransactionTime(java.time.LocalDateTime.now());
    // Mock the modelPredictService to simulate API failure (return 0.0)
    when(modelPredictService.getRiskScore(org.mockito.ArgumentMatchers.any())).thenReturn(0.0);
    mockMvc.perform(post("/api/transaction")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.riskScore").value(0.0));
  }

  @Test
  void testProcessTransaction_RiskScoreInResponse() throws Exception {
    TransactionRequest request = new TransactionRequest();
    request.setTransactionId("txn-riskscore");
    request.setAmount(200.0);
    request.setCurrency("USD");
    request.setAccountId("acc-999");
    request.setTransactionType("POS");
    request.setTransactionTime(java.time.LocalDateTime.now());
    mockMvc.perform(post("/api/transaction")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.riskScore").exists());
  }
}
