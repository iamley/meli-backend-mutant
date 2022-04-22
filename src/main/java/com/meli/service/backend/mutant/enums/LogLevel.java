package com.meli.service.backend.mutant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogLevel {
    INFO(1, "INFO"),
    ERROR(2, "ERROR"),
    DEBUG(3, "DEBUG");

    private final int code;
    private final String description;

}