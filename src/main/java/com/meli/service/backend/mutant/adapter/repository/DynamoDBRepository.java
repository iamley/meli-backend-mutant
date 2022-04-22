package com.meli.service.backend.mutant.adapter.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.meli.service.backend.mutant.adapter.repository.dto.MutantDTO;
import com.meli.service.backend.mutant.exception.BusinessCapabilityException;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.meli.service.backend.mutant.enums.MLStatus.DATABASE_ERROR;
import static com.meli.service.backend.mutant.enums.MLStatus.DB_ERROR;

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
            LOGGER.error("Error saving object {}" , exception.getMessage());
            throw new BusinessCapabilityException(DB_ERROR.getCode(), DB_ERROR.getDescription());
        }

    }

    public Item scanItemsById(String tableName, String id) throws BusinessCapabilityException {

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
            LOGGER.error("Failed to scan object {}" , exception.getMessage());
            throw new BusinessCapabilityException(DATABASE_ERROR.getCode(), DATABASE_ERROR.getDescription());
        }

    }

    public Integer scanCountItems(String tableName, boolean value) throws BusinessCapabilityException {

        try {

            DynamoDB dynamoDB = new DynamoDB(amazonDynamoDBClient);

            Integer totalScannedItemCount = 0;

            Table table = dynamoDB.getTable(tableName);

            Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
            expressionAttributeValues.put(":vl", value);

            ItemCollection<ScanOutcome> items = table.scan("mutant = :vl",
                    "id, dna, mutant",
                    null,
                    expressionAttributeValues);

            LOGGER.info("Request send to dynamo {}", items);

            Iterator<Item> iterator = items.iterator();

            while (iterator.hasNext()) {
                totalScannedItemCount+=1;
                iterator.next();
            }

            LOGGER.info("Quantity of items {}", totalScannedItemCount);

            return totalScannedItemCount;

        } catch (Exception exception) {
            LOGGER.error("Failed to scan object {}" , exception.getMessage());
            throw new BusinessCapabilityException(DATABASE_ERROR.getCode(), DATABASE_ERROR.getDescription());
        }

    }
}