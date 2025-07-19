package com.frauddetection.controller;

import com.frauddetection.dto.TransactionRequest;
import com.frauddetection.dto.TransactionResponse;
import com.frauddetection.rules.RuleService;
import com.frauddetection.service.TransactionService;
import com.frauddetection.service.ModelPredictService;
import com.frauddetection.dto.ModelPredictRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.GlobalOpenTelemetry;

@RestController
@RequestMapping("/api")
public class TransactionController {
  private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

  private final RuleService ruleService;
  private final TransactionService transactionService;
  private final ModelPredictService modelPredictService;

  public TransactionController(RuleService ruleService, TransactionService transactionService,
      ModelPredictService modelPredictService) {
    this.ruleService = ruleService;
    this.transactionService = transactionService;
    this.modelPredictService = modelPredictService;
  }

  @PostMapping("/transaction")
  public ResponseEntity<TransactionResponse> processTransaction(
      @jakarta.validation.Valid @RequestBody TransactionRequest request) {
    // Start a span for tracing
    Tracer tracer = GlobalOpenTelemetry.getTracer("fraud-detection-engine");
    Span span = tracer.spanBuilder("processTransaction").startSpan();
    try {
      SpanContext ctx = span.getSpanContext();
      logger.info("Received transaction request: {}, traceId={}, spanId={}",
          request.getTransactionId(),
          ctx.isValid() ? ctx.getTraceId() : "",
          ctx.isValid() ? ctx.getSpanId() : "");

      // Prepare model predict request
      ModelPredictRequest predictRequest = new ModelPredictRequest();
      predictRequest.setAmount(request.getAmount());
      predictRequest.setCardType(request.getCardNumber() != null ? "credit" : "debit"); // Example logic
      predictRequest.setMerchantCategory(request.getMerchantId() != null ? request.getMerchantId() : "");
      predictRequest.setCountry("US"); // You may want to map this from request

      // Call model predict API
      double riskScore = modelPredictService.getRiskScore(predictRequest);
      System.out.println("Risk score for transaction " + request.getTransactionId() + ": " + riskScore);
      // Evaluate rules with riskScore in context
      request.setRiskScore(riskScore);
      boolean flagged = ruleService.evaluate(request);

      transactionService.saveTransaction(request, flagged, riskScore);
      TransactionResponse response = new TransactionResponse(request.getTransactionId(), riskScore,
          flagged ? "FLAGGED" : "SUCCESS");
      return ResponseEntity.ok(response);
    } finally {
      span.end();
    }
  }
}
