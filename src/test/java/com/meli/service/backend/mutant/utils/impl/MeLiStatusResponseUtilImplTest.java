package com.meli.service.backend.mutant.utils.impl;

import com.meli.service.backend.mutant.enums.ServerErrorCommon;
import com.meli.service.backend.mutant.exception.BusinessCapabilityException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MeLiStatusResponseUtilImplTest {

    @InjectMocks
    private MeLiStatusResponseUtilImpl util;

    @Mock
    private CircuitBreaker circuitBreaker;

    @Mock
    private HttpInputMessage httpInputMessage;

    @BeforeEach
    public void setUp() {
        circuitBreaker = mock(CircuitBreaker.class, RETURNS_DEEP_STUBS);
        httpInputMessage = mock(HttpInputMessage.class);
    }

    @Test
    void testgetStatusResponseException() throws Exception {

        var response = util.getStatusResponse(new NullPointerException("Test"));

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getHttpStatus());
        Assertions.assertEquals(ServerErrorCommon.INTERNAL_ERROR.getHttpStatus(), response.getHttpStatus());
    }

    @Test
    void testgetStatusResponseBusinessCapabilityException() throws Exception {

        var response = util.getStatusResponse(new BusinessCapabilityException("code", "description", HttpStatus.INTERNAL_SERVER_ERROR));

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getHttpStatus());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getHttpStatus());
    }

    @Test
    void testgetStatusResponseCallNotPermittedException() throws Exception {

        var response = util.getStatusResponse(CallNotPermittedException.createCallNotPermittedException(circuitBreaker));

        // Assert the response
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getHttpStatus());
        Assertions.assertEquals(ServerErrorCommon.CIRCUIT_BREAKER.getHttpStatus(), response.getHttpStatus());
    }

    @Test
    void testgetStatusResponseMethodArgumentTypeMismatchException() throws Exception {

        var response = util.getStatusResponse(new HttpMessageNotReadableException("test", httpInputMessage));

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getHttpStatus());
        Assertions.assertEquals(ServerErrorCommon.REQUEST_FAILED.getHttpStatus(), response.getHttpStatus());
    }

    @Test
    void testgetStatusServletRequestBindingException() throws Exception {

        var response = util.getStatusResponse(new ServletRequestBindingException("test"));

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getHttpStatus());
        Assertions.assertEquals(ServerErrorCommon.REQUEST_FAILED.getHttpStatus(), response.getHttpStatus());
    }

    @Test
    void testgetStatusMissingServletRequestParameterException() throws Exception {

        var response = util.getStatusResponse(new MissingServletRequestParameterException("test", "test"));

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getHttpStatus());
        Assertions.assertEquals(ServerErrorCommon.REQUEST_FAILED.getHttpStatus(), response.getHttpStatus());
    }

}