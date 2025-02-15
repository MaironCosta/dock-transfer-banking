package com.dock.transferbanking.exception;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Provides handling for exceptions throughout this service. */
    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity<ApiError> handleNotFoundException(Exception ex, WebRequest request) {

        NotFoundException val = (NotFoundException) ex;
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return this.handleExceptionInternal(ex, new ApiError(val.getMessage()), headers, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({InternalServerErrorException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<ApiError> handleInternalServerErrorException(Exception ex, WebRequest request) {

        InternalServerErrorException isre = (InternalServerErrorException) ex;
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return this.handleExceptionInternal(ex, new ApiError(isre.getMessage()), headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({ValidationException.class})
    public final ResponseEntity<ApiError> handleValidationException(Exception ex, WebRequest request) {

        ValidationException val = (ValidationException) ex;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return this.handleExceptionInternal(ex, new ApiError("error"), headers, val.statusCode, request);
    }

    /** A single place to customize the response body of all Exception types. */
    protected ResponseEntity<ApiError> handleExceptionInternal(Exception ex, ApiError body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
    }
}
