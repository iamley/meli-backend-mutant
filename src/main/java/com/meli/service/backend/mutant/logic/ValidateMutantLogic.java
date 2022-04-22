package com.meli.service.backend.mutant.logic;

import com.meli.service.backend.mutant.controller.dto.ValidateMutantDTO;

public interface ValidateMutantLogic {
    Boolean invoke(ValidateMutantDTO dna);
}