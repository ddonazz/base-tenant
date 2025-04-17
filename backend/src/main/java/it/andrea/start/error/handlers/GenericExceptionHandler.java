package it.andrea.start.error.handlers;

import it.andrea.start.controller.response.InternalServerErrorResponse;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.RollbackException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GenericExceptionHandler extends AbstractHandler {

    private final MessageSource messageSource;

    public GenericExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({
            RollbackException.class,
            PersistenceException.class,
            ConstraintViolationException.class,
            NullPointerException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Object> toResponse(Exception exception) {
        String errorMessage = messageSource.getMessage(
                resolveMessageKey(exception).getCode(),
                null,
                "Generic error occurred",
                LocaleContextHolder.getLocale()
        );

        return ResponseEntity.internalServerError().body(new InternalServerErrorResponse(exception.getMessage(), errorMessage));
    }

    @Override
    public String getEntityType() {
        throw new UnsupportedOperationException();
    }
}