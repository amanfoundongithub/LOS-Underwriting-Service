package com.loan_org.underwriting_service.evaluation.model;

import java.time.Instant;

import org.bson.Document;

import lombok.Builder;

@Builder
public record BureauSnapshot(
    String bureauName,
    int creditScore,
    Document rawResponse, 
    Instant fetchedAt
) {}
