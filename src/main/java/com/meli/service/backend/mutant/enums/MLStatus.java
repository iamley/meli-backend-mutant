package com.meli.service.backend.mutant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MLStatus {
    SUCCEED("0000", "Service executed successfully."),
    FATAL_ERROR("0001", "Unexpected error in service execution."),
    DB_ERROR("0002", "Error saving data to database."),
    EMPTY_BODY("0003", "The information received is invalid."),
    EMPTY("0004","No data found to display status."),
    DATABASE_ERROR("0005", "Failed to connect to database."),
    INVALID_BODY("0006", "Error mapping the request."),
    BAD_REQUEST("0007", "Bad request, missing input data in the request.");

    private final String code;
    private final String description;

}