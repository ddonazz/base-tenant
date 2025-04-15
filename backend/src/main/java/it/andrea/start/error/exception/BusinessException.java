package it.andrea.start.error.exception;

public class BusinessException extends Exception {

    private static final long serialVersionUID = 3278937856043871034L;

    private String entity;
    private String code;
    private String[] messageSubString;

    public BusinessException(String entity, String codice, String[] messageSubString) {
        super();
        this.entity = entity;
        this.code = codice;
        this.messageSubString = messageSubString;
    }

    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }

    public BusinessException(String entity, String message, String codice, String... messageComponent) {
        super(message);
        this.entity = entity;
        this.code = codice;
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

    public String getCodice() {
        return code;
    }

    public void setCodice(String codice) {
        this.code = codice;
    }

    public String[] getMessageSubString() {
        return messageSubString;
    }

    public void setMessageSubString(String[] messageSubString) {
        this.messageSubString = messageSubString;
    }

}
