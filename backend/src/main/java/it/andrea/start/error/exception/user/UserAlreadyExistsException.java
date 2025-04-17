package it.andrea.start.error.exception.user;

import java.io.Serial;

public class UserAlreadyExistsException extends UserException {

    @Serial
    private static final long serialVersionUID = 70786780815501035L;

    public UserAlreadyExistsException(String userId) {
        super(userId, "User " + userId + " already exists");
    }

}
