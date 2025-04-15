package it.andrea.start.error.handlers;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import it.andrea.start.controller.response.InternalServerErrorResponse;
import it.andrea.start.error.exception.ErrorCode;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.RollbackException;

@ControllerAdvice
public class GenericExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GenericExceptionHandler.class);

    private static final Map<Class<? extends Exception>, ErrorCode> EXCEPTION_MESSAGE_KEYS = Map.of( //
            RollbackException.class, ErrorCode.ERROR_TRANSACTION_ROLLED_BACK, //
            PersistenceException.class, ErrorCode.ERROR_PERSISTENCE_EXCEPTION, //
            ConstraintViolationException.class, ErrorCode.ERROR_CONSTRAINT_VIOLATION, //
            NullPointerException.class, ErrorCode.ERROR_NULL_POINTER //
    ); //

    @ExceptionHandler({ //
            RollbackException.class, //
            PersistenceException.class, //
            ConstraintViolationException.class, //
            NullPointerException.class //
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Object> toResponse(Exception exception, Locale locale) {
        LOG.error("Exception caught: {}, caused by: {}", exception.getClass().getSimpleName(), exception.getCause(), exception);
        ResourceBundle rb = ResourceBundle.getBundle("bundles.Messages", locale);
        ErrorCode errorCode = resolveMessageKey(exception);
        String message = rb.containsKey(errorCode.getCode()) ? rb.getString(errorCode.getCode()) : "Unexpected error occurred";

        return ResponseEntity.internalServerError().body(new InternalServerErrorResponse(exception.getMessage(), message));
    }

    private ErrorCode resolveMessageKey(Exception exception) {
        return EXCEPTION_MESSAGE_KEYS.getOrDefault(exception.getClass(), ErrorCode.ERROR_INTERNAL_SERVER_ERROR);
    }

}