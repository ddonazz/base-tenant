package it.andrea.start.error.exception.user;

import java.io.Serial;

public class UserNotFoundException extends UserException {

    @Serial
    private static final long serialVersionUID = 6236059369592609596L;

    public UserNotFoundException(String userId) {
        super(userId, "User " + userId + " not found");
    }

}
