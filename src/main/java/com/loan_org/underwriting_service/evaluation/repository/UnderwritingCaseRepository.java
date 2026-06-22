package com.loan_org.underwriting_service.evaluation.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.loan_org.underwriting_service.evaluation.model.UnderwritingCase;

@Repository
public interface UnderwritingCaseRepository extends MongoRepository<UnderwritingCase, String> {
    Optional<UnderwritingCase> findByApplicationId(String applicantId);
}