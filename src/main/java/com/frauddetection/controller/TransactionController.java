package com.frauddetection.controller;

import com.frauddetection.dto.TransactionRequest;
import com.frauddetection.dto.TransactionResponse;
import com.frauddetection.rules.RuleService;
import com.frauddetection.service.TransactionService;
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

  public TransactionController(RuleService ruleService, TransactionService transactionService) {
    this.ruleService = ruleService;
    this.transactionService = transactionService;
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
      // Evaluate rules
      boolean flagged = ruleService.evaluate(request);
      double riskScore = flagged ? 1.0 : Math.random();
      transactionService.saveTransaction(request, flagged);
      TransactionResponse response = new TransactionResponse(request.getTransactionId(), riskScore,
          flagged ? "FLAGGED" : "SUCCESS");
      return ResponseEntity.ok(response);
    } finally {
      span.end();
    }
  }
}
