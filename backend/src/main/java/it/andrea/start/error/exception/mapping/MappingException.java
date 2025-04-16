package it.andrea.start.error.exception.mapping;

public abstract class MappingException extends RuntimeException {
    private static final long serialVersionUID = 4380802582542388803L;

    protected MappingException() {
        super();
    }

    protected MappingException(String message) {
        super(message);
    }

    protected MappingException(String message, Throwable cause) {
        super(message, cause);
    }

    protected MappingException(Throwable cause) {
        super(cause);
    }
}
