package com.meli.service.backend.mutant.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.meli.service.backend.mutant.model.StatusDataDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({"status"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponseEntity {
    protected StatusDataDTO status;
}