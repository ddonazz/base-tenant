package it.andrea.start.errorhandlers;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import it.andrea.start.controller.response.ErrorResponse;

@ControllerAdvice
public class ValidationExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationExceptionHandler.class);

    private static final String VALIDATION_FAILED_KEY = "error.validationfailed";
    private static final String DEFAULT_ERROR_MESSAGE = "Validation error occurred";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex, Locale locale) {
	LOG.error("Validation exception occurred", ex);

	ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles.Messages", locale);
	String validationFailedMessage = resourceBundle.containsKey(VALIDATION_FAILED_KEY) ? resourceBundle.getString(VALIDATION_FAILED_KEY) : DEFAULT_ERROR_MESSAGE;

	if (ex.getBindingResult().hasFieldErrors()) {
	    Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, fieldError -> {
		String fieldMessageKey = "error." + fieldError.getField();
		return resourceBundle.containsKey(fieldMessageKey) ? resourceBundle.getString(fieldMessageKey) : fieldError.getDefaultMessage();
	    }));

	    ErrorResponse response = new ErrorResponse(validationFailedMessage, DEFAULT_ERROR_MESSAGE, new ArrayList<>(fieldErrors.values()));

	    return ResponseEntity.badRequest().body(response);
	}

	return ResponseEntity.badRequest().body(new ErrorResponse(validationFailedMessage, DEFAULT_ERROR_MESSAGE, new ArrayList<>()));
    }
}
