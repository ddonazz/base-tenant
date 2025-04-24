package it.andrea.start.error.exception.user;

import java.io.Serial;

import it.andrea.start.error.exception.ApplicationException;
import it.andrea.start.error.exception.ErrorCode;

public class UserAlreadyExistsException extends ApplicationException {

    @Serial
    private static final long serialVersionUID = 70786780815501035L;

    public UserAlreadyExistsException(String userId) {
        super(ErrorCode.USER_ALREADY_EXISTS, "User " + userId + " already exists", userId);
    }

}
