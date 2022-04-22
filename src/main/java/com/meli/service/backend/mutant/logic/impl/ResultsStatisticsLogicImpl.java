package com.meli.service.backend.mutant.logic.impl;

import com.meli.service.backend.mutant.adapter.repository.DynamoDBRepository;
import com.meli.service.backend.mutant.exception.BusinessCapabilityException;
import com.meli.service.backend.mutant.logic.ResultsStatisticsLogic;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.meli.service.backend.mutant.enums.MLStatus.DATABASE_ERROR;
import static com.meli.service.backend.mutant.enums.MLStatus.FATAL_ERROR;

@Slf4j
@Component("ResultsStatisticsLogicImpl")
public class ResultsStatisticsLogicImpl implements ResultsStatisticsLogic {

    private static Logger LOGGER = LoggerFactory.getLogger(ValidateMutantLogicImpl.class);

    private static final String LABEL_ERROR = "Error generate {}";

    @Value("${ml.ws.aws.table}")
    private String tableName;

    @Autowired
    private DynamoDBRepository dynamoDBRepository;

    @Override
    public Integer amountOfMutantOrHuman(Boolean value) {

        Integer count;

        try {
            count = dynamoDBRepository.scanCountItems(tableName, value);
        } catch (Exception e) {
            LOGGER.info(LABEL_ERROR, e);
            throw new BusinessCapabilityException(
                    DATABASE_ERROR.getCode(),
                    DATABASE_ERROR.getDescription());
        }
        return count;
    }

    @Override
    public Integer averageMutants(Integer countMutants, Integer countHumans) {

        Integer average;

        try {
            if(countHumans.equals(0)) {
                average = countMutants/1;
            } else {
                average = countMutants/countHumans;
            }
        } catch (Exception e) {
            LOGGER.info(LABEL_ERROR, e);
            throw new BusinessCapabilityException(
                    FATAL_ERROR.getCode(),
                    FATAL_ERROR.getDescription());
        }
        return average;
    }
}