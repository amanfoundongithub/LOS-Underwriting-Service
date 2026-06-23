package com.loan_org.underwriting_service.shared.security.filter;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.loan_org.underwriting_service.shared.security.service.JwtVerificationService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter {

    private final JwtVerificationService jwtVerificationService;

    private static final String AUTH_HEADER_MISSING_MESSAGE = """
            {
                "error": "UNAUTHORIZED",
                "status": 401,
                "message": "No Authorization header was provided"
            }
            """;
    
    private static final String AUTH_HEADER_NOT_BEARER_TOKEN_MESSAGE = """
            {
                "error": "UNAUTHORIZED",
                "status": 401,
                "message": "No Bearer token was provided in Authorization header. Please provide as `Bearer <access-token>`"
            }
            """;
    
    private static final String UNAUTHORIZED_MESSAGE_TOKEN_EXPIRED = """
            {
                "error": "UNAUTHORIZED",
                "status": 401,
                "message": "The provided Bearer token is expired. Please issue a new access token from IAM service."
            }
            """;

    private static final String UNAUTHORIZED_MESSAGE_TOKEN_INVALID = """
            {
                "error": "UNAUTHORIZED",
                "status": 401,
                "message": "The provided Bearer token is invalid. Please issue a new access token from IAM service."
            }
            """;

    private static final String BEARER_TOKEN_PREFIX = "Bearer ";


    @Override
    public void doFilter(
        ServletRequest request, 
        ServletResponse response, 
        FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null) {
            unAuthorizedResponse(httpResponse, AUTH_HEADER_MISSING_MESSAGE);
            return;
        } 
        if(!authHeader.startsWith(BEARER_TOKEN_PREFIX)) {
            unAuthorizedResponse(httpResponse, AUTH_HEADER_NOT_BEARER_TOKEN_MESSAGE);
            return;
        }

        try {
            String token  = authHeader.substring(BEARER_TOKEN_PREFIX.length());
            Map<String, Object> userMap = jwtVerificationService.getAttributes(token);

            if (!Boolean.TRUE.equals(userMap.get("isAllowed"))) {
                forbiddenResponse(httpResponse);
                return;
            }

            request.setAttribute("userId", userMap.get("userId"));
            chain.doFilter(httpRequest, httpResponse);

        } catch(ExpiredJwtException _) {
            unAuthorizedResponse(httpResponse, UNAUTHORIZED_MESSAGE_TOKEN_EXPIRED);
        } catch(JwtException _) {
            unAuthorizedResponse(httpResponse, UNAUTHORIZED_MESSAGE_TOKEN_INVALID);
        }
    }

    private void unAuthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
        response.getWriter().flush();
    }

    private void forbiddenResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("""
                {
                    "status" : 403,
                    "error" : "FORBIDDEN",
                    "message" : "The given user cannot enter this system as they are not an underwriter."
                }
                """);
        response.getWriter().flush();
    }
    
}
