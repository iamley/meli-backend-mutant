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

    public static LogLevel getValues(int code) {
        for (LogLevel level : LogLevel.values()) {
            if (level.getCode() == code) {
                return level;
            }
        }
        return null;
    }

}