package it.andrea.start.error.handlers;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import it.andrea.start.controller.response.BadRequestResponse;
import it.andrea.start.error.exception.job.JobException;
import lombok.AllArgsConstructor;

@ControllerAdvice
@AllArgsConstructor
public class JobExceptionHandler extends AbstractHandler {

    @Override
    public String getEntityType() {
        return "Job";
    }

    private final MessageSource messageSource;

    @ExceptionHandler(JobException.class)
    public final ResponseEntity<Object> handleUserException(JobException jobException) {
        String errorMessage = messageSource.getMessage(
                resolveMessageKey(jobException).getCode(),
                jobException.getMessageArguments(),
                jobException.getMessage(),
                LocaleContextHolder.getLocale());

        return ResponseEntity.badRequest().body(new BadRequestResponse(getEntityType(), errorMessage));
    }

}
