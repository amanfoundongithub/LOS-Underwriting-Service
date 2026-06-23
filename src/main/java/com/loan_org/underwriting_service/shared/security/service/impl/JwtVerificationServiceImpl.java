package com.loan_org.underwriting_service.shared.security.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.loan_org.underwriting_service.shared.security.service.JwtVerificationService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtVerificationServiceImpl implements JwtVerificationService {

    @Value("${jwt.signing_key}")
    private String signingKey;

    private SecretKey secretKey;

    @PostConstruct
    void init() {
        secretKey = Keys.hmacShaKeyFor(
                signingKey.getBytes(StandardCharsets.UTF_8)
        );
    }
    
    @Override
    public Map<String, Object> getAttributes(String jwtToken) {

        Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();
        
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", claims.getSubject());
        
        // Add attributes also
        @SuppressWarnings("unchecked")
        Map<String, Object> attributes = 
            (Map<String, Object>) claims.getOrDefault("attributes", 
                new HashMap<>()
            );
        if(attributes.get("userRole").equals("UNDERWRITER")) {
            userMap.put("isAllowed", true);
        } else {
            userMap.put("isAllowed", false);
        }

        return userMap;
    }

}
