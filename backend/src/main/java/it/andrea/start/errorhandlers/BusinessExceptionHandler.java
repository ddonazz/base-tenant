package it.andrea.start.errorhandlers;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import it.andrea.start.controller.response.BadRequestResponse;
import it.andrea.start.exception.BusinessException;

@ControllerAdvice
public class BusinessExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Object> toResponse(BusinessException businessException, Locale locale) {
	LOG.error("Catch exception", businessException);

	ResourceBundle rb = ResourceBundle.getBundle("bundles.Messages", locale);
	String message = mapMessage(businessException, rb);

	BadRequestResponse response = new BadRequestResponse(businessException.getEntity(), businessException.getMessage(), message);

	return ResponseEntity.badRequest().body(response);
    }

    private String mapMessage(BusinessException businessException, ResourceBundle rb) {
	String errorCode = businessException.getCodice();

	try {
	    return rb.getString(errorCode);
	} catch (MissingResourceException e) {
	    return businessException.getMessage();
	}
    }

}
