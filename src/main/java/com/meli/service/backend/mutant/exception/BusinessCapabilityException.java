package com.meli.service.backend.mutant.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class BusinessCapabilityException extends RuntimeException {

    private static final long serialVersionUID = -6821998361199415787L;

    private final String errorCodeBusiness;

    private final String errorMessage;

    private final HttpStatus errorHttpStatus;

    private final HttpHeaders httpHeaders;

    private final String responseBody;

    public BusinessCapabilityException() {
        this.errorMessage = null;
        this.errorCodeBusiness = null;
        this.errorHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.httpHeaders = null;
        this.responseBody = null;
    }

    /**
     * Instantiates a new business capability exception.
     *
     * @param errorCodeBusiness the error code business
     * @param errorMessage the error message
     * @param errorHttpStatus the error http status
     */
    public BusinessCapabilityException(String errorCodeBusiness, String errorMessage, HttpStatus errorHttpStatus) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCodeBusiness = errorCodeBusiness;
        this.errorHttpStatus = errorHttpStatus;
        this.httpHeaders = null;
        this.responseBody = null;
    }

    /**
     * Instantiates a new business capability exception.
     *
     * @param errorMessage the error message
     */
    public BusinessCapabilityException(String errorMessage) {
        super(errorMessage);
        this.errorCodeBusiness = null;
        this.errorMessage = errorMessage;
        this.errorHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.httpHeaders = null;
        this.responseBody = null;
    }

    /**
     * Instantiates a new business capability exception.
     *
     * @param errorCodeBusiness the error code business
     * @param errorMessage the error message
     */
    public BusinessCapabilityException(String errorCodeBusiness, String errorMessage) {
        super(errorMessage);
        this.errorCodeBusiness = errorCodeBusiness;
        this.errorMessage = errorMessage;
        this.errorHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.httpHeaders = null;
        this.responseBody = null;
    }

    /**
     * Instantiates a new business capability exception.
     *
     * @param errorMessageBusiness the error message business
     * @param cause the cause
     */
    public BusinessCapabilityException(String errorMessageBusiness, Throwable cause) {
        super(errorMessageBusiness, cause);
        this.errorCodeBusiness = null;
        this.errorMessage = errorMessageBusiness;
        this.errorHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.httpHeaders = null;
        this.responseBody = null;
    }

    /**
     * Instantiates a new business capability exception.
     *
     * @param errorCodeBusiness the error code business
     * @param errorMessage the error message
     * @param cause the cause
     */
    public BusinessCapabilityException(String errorCodeBusiness, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCodeBusiness = errorCodeBusiness;
        this.errorMessage = errorMessage;
        this.errorHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.httpHeaders = null;
        this.responseBody = null;
    }

    /**
     * Instantiates a new business capability exception.
     *
     * @param errorCodeBusiness the error code business
     * @param errorMessage the error message
     * @param errorHttpStatus the errorHttpStatus
     * @param  httpHeaders the httpHeaders
     */
    public BusinessCapabilityException(String errorCodeBusiness, String errorMessage, HttpStatus errorHttpStatus,
                                       HttpHeaders httpHeaders) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCodeBusiness = errorCodeBusiness;
        this.errorHttpStatus = errorHttpStatus;
        this.httpHeaders = httpHeaders;
        this.responseBody = null;
    }

    /**
     * Instantiates a new business capability exception.
     *
     * @param errorCodeBusiness the error code business
     * @param errorMessage the error message
     * @param errorHttpStatus the errorHttpStatus
     * @param  httpHeaders the httpHeaders
     * @param responseBody the responseBody
     */
    public BusinessCapabilityException(String errorCodeBusiness, String errorMessage, HttpStatus errorHttpStatus,
                                       HttpHeaders httpHeaders, String responseBody) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCodeBusiness = errorCodeBusiness;
        this.errorHttpStatus = errorHttpStatus;
        this.httpHeaders = httpHeaders;
        this.responseBody = responseBody;
    }

}
