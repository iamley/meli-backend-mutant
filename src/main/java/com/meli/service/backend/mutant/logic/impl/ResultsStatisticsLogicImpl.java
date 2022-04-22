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

import java.math.BigDecimal;
import java.math.RoundingMode;

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
    public BigDecimal averageMutants(Integer countMutants, Integer countHumans) {

        BigDecimal average;

        try {
            average = BigDecimal.valueOf(countHumans.equals(0) ? countHumans/1 : countMutants/countHumans);
            average.setScale(1, ROUND_MODE);
        } catch (Exception e) {
            LOGGER.info(LABEL_ERROR, e);
            throw new BusinessCapabilityException(
                    FATAL_ERROR.getCode(),
                    FATAL_ERROR.getDescription());
        }
        return average;
    }

    private static final RoundingMode ROUND_MODE = RoundingMode.HALF_EVEN;
}