package it.andrea.start.errorhandlers;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import it.andrea.start.controller.response.BadRequestResponse;
import it.andrea.start.exception.user.UserAlreadyExistsException;
import it.andrea.start.exception.user.UserException;
import it.andrea.start.exception.user.UserNotFoundException;
import it.andrea.start.exception.user.UserRoleAlreadyExistsException;
import it.andrea.start.exception.user.UserRoleNotFoundException;

@ControllerAdvice
public class UserExceptionHandler {

    private static final String ENTITY = "User";
    private static final String MESSAGE_BUNDLE_PATH = "bundles.Messages";

    private static final Map<Class<? extends UserException>, String> EXCEPTION_MESSAGE_KEYS = Map.of(
	    UserAlreadyExistsException.class, "error.user.alreadyexists", 
	    UserNotFoundException.class, "error.user.notfound", 
	    UserRoleAlreadyExistsException.class, "error.userrole.alreadyexists", 
	    UserRoleNotFoundException.class, "error.userrole.notfound");

    @ExceptionHandler(UserException.class)
    public final ResponseEntity<Object> handleUserException(UserException userException, Locale locale) {
	ResourceBundle rb = ResourceBundle.getBundle(MESSAGE_BUNDLE_PATH, locale);
	String messageKey = resolveMessageKey(userException);
	String localizedMessage = rb.getString(messageKey);

	BadRequestResponse response = createBadRequestResponse(userException, localizedMessage);

	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private String resolveMessageKey(UserException userException) {
	return EXCEPTION_MESSAGE_KEYS.getOrDefault(userException.getClass(), "error.generic");
    }

    private BadRequestResponse createBadRequestResponse(UserException userException, String message) {
	return new BadRequestResponse(ENTITY, userException.getMessage(), message);
    }
}
