package com.loan_org.underwriting_service.features.orchestrator.controller;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loan_org.underwriting_service.features.orchestrator.service.UnderwritingOrchestratorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/underwriting/evaluate")
public class UnderwritingOrchestratorController {

    private final UnderwritingOrchestratorService orchestratorService;
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> process(
        @RequestParam("applicationId") String applicationId 
    ) {
        log.info("Received request to evaluate for applicationId: {}", 
            applicationId
        );

        CompletableFuture.runAsync(() -> {
            try {
                orchestratorService.process(applicationId);
                log.info("Successfully completed underwriting evaluation for applicationId: {}", applicationId);
            } catch (Exception e) {
                log.error("Asynchronous underwriting failed for applicationId: {}", applicationId, e);
            }
        });

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of(
                    "correlationId", applicationId,
                    "status", "PROCESSING",
                    "message", "Underwriting assessment started asynchronously."
                ));
    }
}
