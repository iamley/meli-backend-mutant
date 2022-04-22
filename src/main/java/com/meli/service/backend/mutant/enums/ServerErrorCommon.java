package com.meli.service.backend.mutant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ServerErrorCommon {

    INTERNAL_ERROR("X0001", "Unknown Error.", HttpStatus.INTERNAL_SERVER_ERROR),
    REQUEST_FAILED("X0002", "Error in the request or some field may be empty.", HttpStatus.BAD_REQUEST),
    FALLBACK("X0003", "Please try again, we are experiencing some issues.", HttpStatus.SERVICE_UNAVAILABLE),
    CIRCUIT_BREAKER("X0004", "Error, services are currently unavailable, please try again.", HttpStatus.SERVICE_UNAVAILABLE);

    private String code;
    private String message;
    private HttpStatus httpStatus;

}