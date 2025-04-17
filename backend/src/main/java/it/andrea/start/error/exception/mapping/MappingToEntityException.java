package it.andrea.start.error.exception.mapping;

import java.io.Serial;

public class MappingToEntityException extends MappingException {

    @Serial
    private static final long serialVersionUID = 6167561463983215473L;

    public MappingToEntityException() {
        super();
    }

    public MappingToEntityException(String message) {
        super(message);
    }

    public MappingToEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappingToEntityException(Throwable cause) {
        super(cause);
    }

}
