package com.meli.service.backend.mutant.logic;

public interface ResultsStatisticsLogic {

    Integer amountOfMutantOrHuman(Boolean value);
    Float averageMutants(Integer countMutants, Integer countHumans);

}