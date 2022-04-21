package com.meli.service.backend.mutant.adapter.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MutantDTO {

    private String id = null;
    private List<String> dna = null;
    private Boolean isMutant = null;
}
