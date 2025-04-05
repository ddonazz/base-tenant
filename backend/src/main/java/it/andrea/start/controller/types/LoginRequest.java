package it.andrea.start.controller.types;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 3903555349152727298L;

    @NotBlank(message = "L'username non può essere vuoto")
    private String username;

    @NotBlank(message = "La password non può essere vuota")
    private String password;

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

}
