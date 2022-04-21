package com.meli.service.backend.mutant.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class BusinessCapabilityExceptionTest {

    @Mock
    private BusinessCapabilityException businessCapabilityException;

    private HttpHeaders httpHeaders;

    private static final String code = "0001";
    private static final String description = "Se generó una excepción";

    private static final String responseBody = "{\"json\": \"key\"}";

    @BeforeEach
    void setup() {
        httpHeaders = new HttpHeaders();
    }

    @Test
    void codeDescriptionHttpWhenOfWrapper() {
        assertThatThrownBy(() -> {
            throw new BusinessCapabilityException(code, description, HttpStatus.BAD_GATEWAY);
        }).isInstanceOf(BusinessCapabilityException.class).hasStackTraceContaining("BusinessCapabilityException");
    }

    @Test
    void descriptionWhenOfWrapper() {
        assertThatThrownBy(() -> {
            throw new BusinessCapabilityException(description);

        }).isInstanceOf(BusinessCapabilityException.class).hasStackTraceContaining("BusinessCapabilityException");
    }

    @Test
    void descriptionWhenEmpty() {
        assertThatThrownBy(() -> {
            throw new BusinessCapabilityException();

        }).isInstanceOf(BusinessCapabilityException.class).hasStackTraceContaining("BusinessCapabilityException");
    }

    @Test
    void descriptionThrowableWhenOfWrapper() {
        assertThatThrownBy(() -> {
            throw new BusinessCapabilityException(description, new Throwable());
        }).isInstanceOf(BusinessCapabilityException.class).hasStackTraceContaining("BusinessCapabilityException");
    }

    @Test
    void codeDescriptionThrowableWhenOfWrapper() {
        assertThatThrownBy(() -> {
            throw new BusinessCapabilityException(code, description, new Throwable(code));
        }).isInstanceOf(BusinessCapabilityException.class).hasStackTraceContaining("BusinessCapabilityException");
    }

    @Test
    void codeDescriptionWhenOfWrapper() {
        assertThatThrownBy(() -> {
            throw new BusinessCapabilityException(code, description);
        }).isInstanceOf(BusinessCapabilityException.class).hasStackTraceContaining("BusinessCapabilityException");
    }

    @Test
    void codeDescriptionFull() {
        assertThatThrownBy(() -> {
            throw new BusinessCapabilityException(code, description, HttpStatus.INTERNAL_SERVER_ERROR, httpHeaders);
        }).isInstanceOf(BusinessCapabilityException.class).hasStackTraceContaining("BusinessCapabilityException");
    }

    @Test
    void codeDescriptionResponseBodyFull() {
        assertThatThrownBy(() -> {
            throw new BusinessCapabilityException(code, description, HttpStatus.INTERNAL_SERVER_ERROR, httpHeaders, responseBody);
        }).isInstanceOf(BusinessCapabilityException.class).hasStackTraceContaining("BusinessCapabilityException");
    }

}