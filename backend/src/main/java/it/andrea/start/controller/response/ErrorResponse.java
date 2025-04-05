package it.andrea.start.controller.response;

import java.io.Serializable;
import java.util.List;

public class ErrorResponse implements Serializable {
    private static final long serialVersionUID = 420009367930874140L;

    public ErrorResponse() {
	this.message = "";
	this.code = "";
	this.details = null;
    }

    public ErrorResponse(String message, String code, List<String> details) {
	super();
	this.message = message;
	this.code = code;
	this.details = details;
    }

    private String message;
    private String code;
    private List<String> details;

    // Getter and setters
    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public List<String> getDetails() {
	return details;
    }

    public void setDetails(List<String> details) {
	this.details = details;
    }

}
