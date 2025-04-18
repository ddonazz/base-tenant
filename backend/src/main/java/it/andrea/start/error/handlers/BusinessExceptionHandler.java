package it.andrea.start.error.handlers;

import it.andrea.start.controller.response.BadRequestResponse;
import it.andrea.start.error.exception.BusinessException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BusinessExceptionHandler extends AbstractHandler {

    private final MessageSource messageSource;

    public BusinessExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Object> toResponse(BusinessException businessException) {
        String errorMessage = messageSource.getMessage(
                businessException.getMessage(),
                null,
                "Generic error occurred while processing request",
                LocaleContextHolder.getLocale()
        );

        return ResponseEntity.badRequest().body(new BadRequestResponse(businessException.getEntity(), errorMessage));
    }

    @Override
    public String getEntityType() {
        throw new UnsupportedOperationException();
    }

}
