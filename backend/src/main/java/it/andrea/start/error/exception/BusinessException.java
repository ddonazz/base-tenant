package it.andrea.start.error.exception;

import java.io.Serial;

public class BusinessException extends Exception {

    @Serial
    private static final long serialVersionUID = 3278937856043871034L;

    private String entity;
    private final String code;
    private String[] messageSubString;

    public BusinessException(String entity, String message, String code, String... messageComponent) {
        super(message);
        this.entity = entity;
        this.code = code;
        if (messageComponent != null && messageComponent.length > 0) {
            this.messageSubString = messageComponent;
        }
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getCode() {
        return code;
    }

}
