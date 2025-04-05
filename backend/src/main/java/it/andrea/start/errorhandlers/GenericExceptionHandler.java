package it.andrea.start.errorhandlers;

import java.sql.SQLException;
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
import jakarta.persistence.PersistenceException;
import jakarta.transaction.RollbackException;

@ControllerAdvice
public class GenericExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GenericExceptionHandler.class);

    private static final Map<Class<? extends Exception>, String> EXCEPTION_MESSAGE_KEYS = Map.of(
	    RollbackException.class, "error.transactionrolledback", 
	    PersistenceException.class, "error.persistenceexception", 
	    ConstraintViolationException.class, "error.constraintviolation", 
	    NullPointerException.class, "error.nullpointer");

    private static final String GENERIC_MESSAGE_KEY = "error.internalservererror";
    private static final String SQL_GENERICERROR_KEY = "error.sqlgeneric";
    private static final String SQL_ERROR_PREFIX = "error.sql";

    @ExceptionHandler({ RollbackException.class, PersistenceException.class, ConstraintViolationException.class, NullPointerException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Object> toResponse(Exception exception, Locale locale) {
	LOG.error("Exception caught: {}, caused by: {}", exception.getClass().getSimpleName(), exception.getCause(), exception);
	ResourceBundle rb = ResourceBundle.getBundle("bundles.Messages", locale);
	String messageKey = EXCEPTION_MESSAGE_KEYS.getOrDefault(exception.getClass(), GENERIC_MESSAGE_KEY);
	if (exception instanceof SQLException sqlException) {
	    String sqlKey = SQL_ERROR_PREFIX + sqlException.getErrorCode();
	    if (rb.containsKey(sqlKey)) {
		messageKey = sqlKey;
	    } else {
		messageKey = SQL_GENERICERROR_KEY;
	    }
	}
	String message = rb.containsKey(messageKey) ? rb.getString(messageKey) : "Unexpected error occurred";

	return ResponseEntity.internalServerError().body(new InternalServerErrorResponse(exception.getMessage(), message));
    }

}