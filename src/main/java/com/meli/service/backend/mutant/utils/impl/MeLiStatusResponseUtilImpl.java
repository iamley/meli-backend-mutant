package com.meli.service.backend.mutant.utils.impl;

import com.meli.service.backend.mutant.enums.ServerErrorCommon;
import com.meli.service.backend.mutant.model.StatusDataDTO;
import com.meli.service.backend.mutant.utils.MeLiStatusResponseUtil;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

@Component("MeLiStatusResponseUtilImpl ")
public class MeLiStatusResponseUtilImpl implements MeLiStatusResponseUtil {

    @Override
    public StatusDataDTO getStatusResponse(final ServerErrorCommon commonError) {
        return StatusDataDTO.builder().code(commonError.getCode()).description(commonError.getMessage())
                .httpStatus(commonError.getHttpStatus()).build();
    }

    @Override
    public StatusDataDTO getStatusResponse(final Throwable throwable) {
        return getStatusResponse(ServerErrorCommon.INTERNAL_ERROR, throwable);
    }

    @Override
    public StatusDataDTO getStatusResponse(ServerErrorCommon commonError, Throwable throwable) {


        if (throwable instanceof CallNotPermittedException) {
            return getStatusResponse(ServerErrorCommon.CIRCUIT_BREAKER);
        }

        if (throwable instanceof MethodArgumentTypeMismatchException) {
            return getStatusResponse(ServerErrorCommon.REQUEST_FAILED);
        }

        if (throwable instanceof MethodArgumentNotValidException) {
            return getStatusResponse(ServerErrorCommon.REQUEST_FAILED);
        }

        if (throwable instanceof HttpMessageNotReadableException) {
            return getStatusResponse(ServerErrorCommon.REQUEST_FAILED);
        }

        if (throwable instanceof ServletRequestBindingException) {
            return getStatusResponse(ServerErrorCommon.REQUEST_FAILED);
        }

        if (throwable instanceof MissingServletRequestParameterException) {
            return getStatusResponse(ServerErrorCommon.REQUEST_FAILED);
        }

        return getStatusResponse(commonError);
    }
}