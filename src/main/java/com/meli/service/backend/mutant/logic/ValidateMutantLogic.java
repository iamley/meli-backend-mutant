package com.meli.service.backend.mutant.logic;

import com.meli.service.backend.mutant.controller.dto.ValidateMutantDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantOutputDTO;

public interface ValidateMutantLogic {
    ValidateMutantOutputDTO invoke(ValidateMutantDTO dna);
}