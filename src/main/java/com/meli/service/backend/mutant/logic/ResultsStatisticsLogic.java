package com.meli.service.backend.mutant.logic;

import java.math.BigDecimal;

public interface ResultsStatisticsLogic {

    Integer amountOfMutantOrHuman(Boolean value);
    BigDecimal averageMutants(Integer countMutants, Integer countHumans);

}