package com.meli.service.backend.mutant.adapter.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.meli.service.backend.mutant.adapter.repository.dto.MutantDTO;
import com.meli.service.backend.mutant.exception.BusinessCapabilityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.meli.service.backend.mutant.enums.MLStatus.DATABASE_ERROR;

@Repository
public class DynamoDBRepository {

    @Autowired
    private AmazonDynamoDBClient amazonDynamoDBClient;

    private static Logger LOGGER = LoggerFactory.getLogger(DynamoDBRepository.class);

    public PutItemOutcome saveItem(String tableName, MutantDTO data) throws BusinessCapabilityException {

        try {

            DynamoDB dynamoDB = new DynamoDB(amazonDynamoDBClient);

            Table table = dynamoDB.getTable(tableName);

            Item item = new Item()
                    .withPrimaryKey("id", data.getId())
                    .withList("dna", data.getDna())
                    .withBoolean("mutant", data.getIsMutant());
            LOGGER.info("Request save to dynamo {}", item);
            table.putItem(item);
            PutItemOutcome outcome = table.putItem(item);
            LOGGER.info("Response send to dynamo {}", outcome);

            return outcome;

        } catch (Exception exception) {
            LOGGER.error("Error al guardar el objeto {}" , exception.getMessage());
            throw new BusinessCapabilityException(DATABASE_ERROR.getCode(), DATABASE_ERROR.getDescription());
        }

    }

    public Item scanItemById(String tableName, String id) throws BusinessCapabilityException {

        try {

            DynamoDB dynamoDB = new DynamoDB(amazonDynamoDBClient);

            GetItemSpec spec = new GetItemSpec()
                    .withPrimaryKey("id", id);
            LOGGER.info("Request send to dynamo {}", spec);

            Table table = dynamoDB.getTable(tableName);
            Item outcome = table.getItem(spec);
            LOGGER.info("Response send to dynamo {}", outcome);

            return outcome;

        } catch (Exception exception) {
            LOGGER.error("Error al escanear el objeto {}" , exception.getMessage());
            throw new BusinessCapabilityException(DATABASE_ERROR.getCode(), DATABASE_ERROR.getDescription());
        }

    }
}