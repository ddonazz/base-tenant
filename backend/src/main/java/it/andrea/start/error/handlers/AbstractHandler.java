package it.andrea.start.error.handlers;

import it.andrea.start.error.exception.ErrorCode;
import it.andrea.start.error.exception.user.UserAlreadyExistsException;
import it.andrea.start.error.exception.user.UserNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.RollbackException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Map;

public abstract class AbstractHandler {

    private static final Map<Class<? extends Exception>, ErrorCode> EXCEPTION_MESSAGE_KEYS = Map.of(
            RollbackException.class, ErrorCode.ERROR_TRANSACTION_ROLLED_BACK,
            PersistenceException.class, ErrorCode.ERROR_PERSISTENCE_EXCEPTION,
            ConstraintViolationException.class, ErrorCode.ERROR_CONSTRAINT_VIOLATION,
            NullPointerException.class, ErrorCode.ERROR_NULL_POINTER,
            UserAlreadyExistsException.class, ErrorCode.USER_ALREADY_EXISTS,
            UserNotFoundException.class, ErrorCode.USER_NOT_FOUND
    );

    ErrorCode resolveMessageKey(Exception exception) {
        return EXCEPTION_MESSAGE_KEYS.getOrDefault(exception.getClass(), ErrorCode.ERROR_INTERNAL_SERVER_ERROR);
    }

    abstract public String getEntityType();
}
