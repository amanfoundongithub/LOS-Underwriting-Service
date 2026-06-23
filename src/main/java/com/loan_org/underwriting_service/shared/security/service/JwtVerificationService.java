package com.loan_org.underwriting_service.shared.security.service;

import java.util.Map;

public interface JwtVerificationService {
    Map<String, Object> getAttributes(String jwtToken);
}
