package com.loan_org.underwriting_service.shared.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Defines the MongoDB configuration associated with the Underwriting 
 * Application. 
 * 
 * Enables Mongo auditing to allow for the dynamic update of creating and updated
 * timestamps.
 * 
 * Enables timestamps by using auto-index-creation = true as part of the bean creation
 * for mongo mapper context.
 * 
 * @author amanfoundongithub
 * @version 1.0.0
 * 
 * @category Configuration
 * 
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {
    
}
