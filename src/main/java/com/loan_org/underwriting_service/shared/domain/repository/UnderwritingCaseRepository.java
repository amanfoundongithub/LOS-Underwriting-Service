package com.loan_org.underwriting_service.shared.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.loan_org.underwriting_service.shared.domain.model.UnderwritingCase;
import com.loan_org.underwriting_service.shared.domain.model.UnderwritingStatus;

/**
 * Data access layer to manage the UnderwritingCase documents in MongoDB, acting 
 * as a driver.
 * 
 * @author amanfoundongithub
 * @version 1.0.0
 * 
 * @category REPOSITORY
 */
@Repository
public interface UnderwritingCaseRepository extends MongoRepository<UnderwritingCase, String> {
    Optional<UnderwritingCase> findByApplicationId(String applicantId);
    Optional<UnderwritingCase> findByCorrelationId(String correlationId);

    List<UnderwritingCase>     findByStatus(UnderwritingStatus status);
    List<UnderwritingCase>     findByCustomerIdOrderByCreatedAtDesc(String customerId);
}