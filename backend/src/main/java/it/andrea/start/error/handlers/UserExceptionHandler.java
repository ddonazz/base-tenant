package it.andrea.start.error.handlers;

import it.andrea.start.controller.response.BadRequestResponse;
import it.andrea.start.error.exception.user.UserException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler extends AbstractHandler {

    private final MessageSource messageSource;

    public UserExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(UserException.class)
    public final ResponseEntity<Object> handleUserException(UserException userException) {
        String errorMessage = messageSource.getMessage(
                resolveMessageKey(userException).getCode(),
                null,
                "Generic error occurred",
                LocaleContextHolder.getLocale()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadRequestResponse(getEntityType(), errorMessage));
    }

    @Override
    public String getEntityType() {
        return "User";
    }
}
