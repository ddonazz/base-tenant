package it.andrea.start.controller.types;

import java.io.Serializable;

public class RecoveryPassword implements Serializable {

    private static final long serialVersionUID = 4654595470927640230L;

    private String email;

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

}
