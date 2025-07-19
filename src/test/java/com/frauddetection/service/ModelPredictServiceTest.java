package com.frauddetection.service;

import com.frauddetection.dto.ModelPredictRequest;
import com.frauddetection.dto.ModelPredictResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ModelPredictServiceTest {
  private ModelPredictService modelPredictService;
  @Mock
  private RestTemplate restTemplate;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    modelPredictService = new ModelPredictService(restTemplate);
    ReflectionTestUtils.setField(modelPredictService, "predictUrl", "http://127.0.0.1:8000/predict");
  }

  @Test
  void testGetRiskScoreSuccess() {
    ModelPredictRequest req = new ModelPredictRequest();
    ModelPredictResponse resp = new ModelPredictResponse();
    resp.setRisk_score(0.42);
    ResponseEntity<ModelPredictResponse> responseEntity = new ResponseEntity<>(resp, HttpStatus.OK);
    when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ModelPredictResponse.class)))
        .thenReturn(responseEntity);
    double score = modelPredictService.getRiskScore(req);
    assertEquals(0.42, score, 0.0001);
  }

  @Test
  void testGetRiskScoreApiFailure() {
    ModelPredictRequest req = new ModelPredictRequest();
    when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ModelPredictResponse.class)))
        .thenThrow(new RuntimeException("API down"));
    double score = modelPredictService.getRiskScore(req);
    assertEquals(0.0, score, 0.0001);
  }

  @Test
  void testGetRiskScoreNullBody() {
    ModelPredictRequest req = new ModelPredictRequest();
    ResponseEntity<ModelPredictResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
    when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ModelPredictResponse.class)))
        .thenReturn(responseEntity);
    double score = modelPredictService.getRiskScore(req);
    assertEquals(0.0, score, 0.0001);
  }
}
