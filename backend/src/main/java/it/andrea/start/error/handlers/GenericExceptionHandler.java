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
import it.andrea.start.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GenericExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GenericExceptionHandler.class);

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorCode errorCode = ErrorCode.ERROR_INTERNAL_SERVER_ERROR;

        String message = messageSource.getMessage(
                errorCode.getCode(),
                null,
                "An unexpected error occurred",
                LocaleContextHolder.getLocale());

        ApiError apiError = new ApiError(
                status,
                errorCode,
                message,
                ((ServletWebRequest) request).getRequest().getRequestURI());

        LOG.error("Unhandled Exception Occurred: Path={}, Message={}", apiError.getPath(), ex.getMessage(), ex);

        return new ResponseEntity<>(apiError, status);
    }

}
