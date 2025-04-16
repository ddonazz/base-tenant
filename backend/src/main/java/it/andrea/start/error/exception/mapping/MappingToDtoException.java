package it.andrea.start.error.exception.mapping;

public class MappingToDtoException extends MappingException {
    private static final long serialVersionUID = -7137814005428126395L;

    public MappingToDtoException() {
        super();
    }

    public MappingToDtoException(String message) {
        super(message);
    }

    public MappingToDtoException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappingToDtoException(Throwable cause) {
        super(cause);
    }

}
