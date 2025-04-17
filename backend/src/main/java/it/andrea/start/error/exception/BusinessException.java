package it.andrea.start.error.exception;

import java.io.Serial;

public class BusinessException extends Exception {

    @Serial
    private static final long serialVersionUID = 3278937856043871034L;

    private final String entity;
    private final ErrorCode code;

    public BusinessException(String entity, ErrorCode errorCode) {
        super();
        this.entity = entity;
        this.code = errorCode;
    }

    public String getEntity() {
        return entity;
    }

    public ErrorCode getCode() {
        return code;
    }

}
