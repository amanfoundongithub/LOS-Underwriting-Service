package com.loan_org.underwriting_service.features.initialization.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loan_org.underwriting_service.features.initialization.dto.UnderwritingTriggerRequest;
import com.loan_org.underwriting_service.features.initialization.dto.UnderwritingTriggerResponse;
import com.loan_org.underwriting_service.features.initialization.service.UnderwritingInitializationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/underwriting/trigger")
public class UnderwritingInitializationController {
    
    private final UnderwritingInitializationService service;

    @PostMapping
    public ResponseEntity<UnderwritingTriggerResponse> initiatePipeline(
        @Valid @RequestBody UnderwritingTriggerRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            service.initialize(request)
        );
    }

}
