package it.andrea.start.error.handlers;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import it.andrea.start.controller.response.BadRequestResponse;
import it.andrea.start.error.exception.ErrorCode;
import it.andrea.start.error.exception.user.UserAlreadyExistsException;
import it.andrea.start.error.exception.user.UserException;
import it.andrea.start.error.exception.user.UserNotFoundException;

@ControllerAdvice
public class UserExceptionHandler {

    private static final String ENTITY = "User";
    private static final String MESSAGE_BUNDLE_PATH = "bundles.Messages";

    private static final Map<Class<? extends UserException>, ErrorCode> EXCEPTION_MESSAGE_KEYS = Map.of( //
            UserAlreadyExistsException.class, ErrorCode.USER_ALREADY_EXISTS, //
            UserNotFoundException.class, ErrorCode.USER_NOT_FOUND //
    ); //

    @ExceptionHandler(UserException.class)
    public final ResponseEntity<Object> handleUserException(UserException userException, Locale locale) {
        ResourceBundle rb = ResourceBundle.getBundle(MESSAGE_BUNDLE_PATH, locale);
        ErrorCode errorCode = resolveMessageKey(userException);
        String localizedMessage = rb.getString(errorCode.getCode());

        BadRequestResponse response = createBadRequestResponse(userException, localizedMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private ErrorCode resolveMessageKey(UserException userException) {
        return EXCEPTION_MESSAGE_KEYS.getOrDefault(userException.getClass(), ErrorCode.ERROR_INTERNAL_SERVER_ERROR);
    }

    private BadRequestResponse createBadRequestResponse(UserException userException, String message) {
        return new BadRequestResponse(ENTITY, userException.getMessage(), message);
    }
}
