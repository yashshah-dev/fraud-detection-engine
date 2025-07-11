package com.frauddetection.controller;

import com.frauddetection.dto.TransactionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
}
