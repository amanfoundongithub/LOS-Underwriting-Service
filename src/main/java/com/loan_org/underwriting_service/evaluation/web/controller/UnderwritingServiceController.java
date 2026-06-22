package com.loan_org.underwriting_service.evaluation.web.controller;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loan_org.underwriting_service.evaluation.model.UnderwritingCase;
import com.loan_org.underwriting_service.evaluation.service.UnderwritingService;
import com.loan_org.underwriting_service.evaluation.web.dto.UnderwritingTriggerRequest;
import com.loan_org.underwriting_service.evaluation.web.dto.UnderwritingTriggerResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/underwriting")
public class UnderwritingServiceController {

    private final UnderwritingService underwritingService;

    @PostMapping
    public ResponseEntity<UnderwritingTriggerResponse> trigger(
        @Valid @RequestBody UnderwritingTriggerRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(underwritingService.create(request));
    }

    @PostMapping("/cases/evaluate/{applicationId}")
    public ResponseEntity<Map<String, Object>> process(
        @PathVariable String applicationId 
    ) {
        log.info("Received evaluation request for applicationId: {}", applicationId);

        CompletableFuture.runAsync(() -> {
            try {
                underwritingService.process(applicationId);
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

    @GetMapping
    public ResponseEntity<UnderwritingCase> get(
        @RequestParam("applicationId") String applicationId 
    ) {
        return underwritingService.findByApplicationId(applicationId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
}
