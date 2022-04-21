package com.meli.service.backend.mutant.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    @Value("${ml.ws.aws.region}")
    private String awsRegion;

    private static Logger LOGGER = LoggerFactory.getLogger(AWSConfig.class);

    @Bean
    public AmazonDynamoDBClient getClientDynamo() {
        LOGGER.info("Iniciando cliente DynamoDB");
        return (AmazonDynamoDBClient) AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(awsRegion)
                .build();
    }
}
