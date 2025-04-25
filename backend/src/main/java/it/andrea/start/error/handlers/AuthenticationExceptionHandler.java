package it.andrea.start.error.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import it.andrea.start.controller.response.ApiError;
import it.andrea.start.error.exception.ErrorCode;
import lombok.AllArgsConstructor;

@ControllerAdvice
@AllArgsConstructor
public class AuthenticationExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationExceptionHandler.class);

    private final MessageSource messageSource;

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleApplicationException(AuthenticationException ex, WebRequest request) {
        ErrorCode errorCode = ErrorCode.AUTHENTICATION_FAILED;
        HttpStatus status = errorCode.getHttpStatus();
        String message = messageSource.getMessage(
                errorCode.getCode(),
                null,
                ex.getMessage(),
                LocaleContextHolder.getLocale());

        ApiError apiError = new ApiError(
                status,
                errorCode,
                message,
                ((ServletWebRequest) request).getRequest().getRequestURI());

        LOG.warn("AuthenticationException Occurred: Code={}, Status={}, Path={}, Message={}", errorCode.getCode(), status, apiError.getPath(), message);

        return new ResponseEntity<>(apiError, errorCode.getHttpStatus());
    }

}
