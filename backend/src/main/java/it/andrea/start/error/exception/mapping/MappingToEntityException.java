package it.andrea.start.error.exception.mapping;

public class MappingToEntityException extends MappingException {
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
