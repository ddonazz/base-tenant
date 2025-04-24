package it.andrea.start.error.exception.user;

import java.io.Serial;

import it.andrea.start.error.exception.ApplicationException;
import it.andrea.start.error.exception.ErrorCode;

public class UserNotFoundException extends ApplicationException {

    @Serial
    private static final long serialVersionUID = 6236059369592609596L;

    public UserNotFoundException(Object userId) {
        super(ErrorCode.USER_NOT_FOUND, "User " + userId + " not found", userId);
    }

}
