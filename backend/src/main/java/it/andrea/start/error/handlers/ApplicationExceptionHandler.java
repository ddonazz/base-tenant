package it.andrea.start.error.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import it.andrea.start.controller.response.ApiError;
import it.andrea.start.error.exception.ApplicationException;
import it.andrea.start.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    private final MessageSource messageSource;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiError> handleApplicationException(ApplicationException ex, WebRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        HttpStatus status = errorCode.getHttpStatus();
        String message = messageSource.getMessage(
                errorCode.getCode(),
                ex.getMessageArguments(),
                ex.getMessage(),
                LocaleContextHolder.getLocale());

        ApiError apiError = new ApiError(
                status,
                errorCode,
                message,
                ((ServletWebRequest) request).getRequest().getRequestURI());

        if (ex.getErrorCode().getHttpStatus().is5xxServerError()) {
            LOG.error("ApplicationException Occurred: Code={}, Status={}, Path={}, Message={}", errorCode.getCode(), status, apiError.getPath(), message, ex);
        } else {
            LOG.warn("ApplicationException Occurred: Code={}, Status={}, Path={}, Message={}", errorCode.getCode(), status, apiError.getPath(), message);
        }

        return new ResponseEntity<>(apiError, ex.getErrorCode().getHttpStatus());
    }

}
