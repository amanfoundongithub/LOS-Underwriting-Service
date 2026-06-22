package com.loan_org.underwriting_service.features.bureau_enrichment.service;

import com.loan_org.underwriting_service.features.bureau_enrichment.dto.BureauSnapshot;

public interface BureauEnrichmentService {
    BureauSnapshot fetchProfile(String applicationId);
}
