package com.frauddetection.service;

import com.frauddetection.dto.ModelPredictRequest;
import com.frauddetection.dto.ModelPredictResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ModelPredictService {
  private static final Logger logger = LoggerFactory.getLogger(ModelPredictService.class);
  private static final String PREDICT_URL = "http://127.0.0.1:8000/predict";
  private final RestTemplate restTemplate;

  public ModelPredictService() {
    this.restTemplate = new RestTemplate();
  }

  // For testing/mocking
  public ModelPredictService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public double getRiskScore(ModelPredictRequest request) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<ModelPredictRequest> entity = new HttpEntity<>(request, headers);
      ResponseEntity<ModelPredictResponse> response = restTemplate.postForEntity(
          PREDICT_URL, entity, ModelPredictResponse.class);
      if (response.getStatusCode().is2xxSuccessful()) {
        ModelPredictResponse body = response.getBody();
        if (body != null) {
          return body.getRisk_score();
        } else {
          logger.warn("Model predict API returned null body: {}", response.getStatusCode());
        }
      } else {
        logger.warn("Model predict API returned non-2xx: {}", response.getStatusCode());
      }
    } catch (Exception ex) {
      logger.error("Error calling model predict API", ex);
    }
    return 0.0; // Default risk score if API fails
  }
}
