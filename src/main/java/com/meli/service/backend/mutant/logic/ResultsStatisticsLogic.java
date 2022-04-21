package com.meli.service.backend.mutant.logic;


import com.meli.service.backend.mutant.controller.dto.StatusMutantOutputDTO;

public interface ResultsStatisticsLogic {
    StatusMutantOutputDTO invoke();
}