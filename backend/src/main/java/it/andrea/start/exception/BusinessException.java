package it.andrea.start.exception;

public class BusinessException extends Exception {

    private static final long serialVersionUID = 3278937856043871034L;

    private String entity;
    private String codice;
    private String[] messageSubString;

    public BusinessException(String entity, String codice, String[] messageSubString) {
	super();
	this.entity = entity;
	this.codice = codice;
	this.messageSubString = messageSubString;
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	this(message, null, cause, enableSuppression, writableStackTrace);
    }

    public BusinessException(String message, Throwable cause) {
	this(message, null, cause);
    }

    public BusinessException(String message) {
	this(message, (String) null);
    }

    public BusinessException(Throwable cause) {
	super(cause);
    }

    public BusinessException(String message, String codice, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
	this.codice = codice;
    }

    public BusinessException(String message, String codice, Throwable cause) {
	super(message, cause);
	this.codice = codice;
    }

    public BusinessException(String message, String codice) {
	super(message);
	this.codice = codice;
    }

    public BusinessException(String entity, String message, String codice, String... messageComponent) {
	super(message);
	this.entity = entity;
	this.codice = codice;
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
	return codice;
    }

    public void setCodice(String codice) {
	this.codice = codice;
    }

    public String[] getMessageSubString() {
	return messageSubString;
    }

    public void setMessageSubString(String[] messageSubString) {
	this.messageSubString = messageSubString;
    }

}
