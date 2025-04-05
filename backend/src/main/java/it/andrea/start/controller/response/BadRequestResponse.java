package it.andrea.start.controller.response;

import java.io.Serializable;

public class BadRequestResponse implements Serializable {

    private static final long serialVersionUID = -7633437953733755422L;

    private final String entity;
    private final String exceptionMessage;
    private final String message;

    public BadRequestResponse(String entity, String exceptionMessage, String message) {
	super();
	this.entity = entity;
	this.exceptionMessage = exceptionMessage;
	this.message = message;
    }

    public String getEntity() {
	return entity;
    }

    public String getExceptionMessage() {
	return exceptionMessage;
    }

    public String getMessage() {
	return message;
    }

    @Override
    public String toString() {
	return "BadRequestResponse [entity=" + entity + ", exceptionMessage=" + exceptionMessage + ", message=" + message + "]";
    }

}
