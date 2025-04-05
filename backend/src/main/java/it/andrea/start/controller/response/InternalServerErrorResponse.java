package it.andrea.start.controller.response;

import java.io.Serializable;

public class InternalServerErrorResponse implements Serializable {

    private static final long serialVersionUID = 798539446484745762L;

    private final String exceptionMessage;
    private final String message;

    public InternalServerErrorResponse(String exceptionMessage, String message) {
	super();
	this.exceptionMessage = exceptionMessage;
	this.message = message;
    }

    public String getExceptionMessage() {
	return exceptionMessage;
    }

    public String getMessage() {
	return message;
    }

    @Override
    public String toString() {
	return "InternalServerErrorResponse [exceptionMessage=" + exceptionMessage + ", message=" + message + "]";
    }

}
