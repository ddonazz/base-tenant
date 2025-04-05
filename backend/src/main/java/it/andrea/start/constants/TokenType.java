package it.andrea.start.constants;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum TokenType {

    TOKEN_RECOVERY_EMAIL("TOKEN_RECOVERY_EMAIL"), TOKEN_CHANGE_EMAIL("TOKEN_CHANGE_EMAIL"), TOKEN_CONFIRM_EMAIL("TOKEN_CONFIRM_EMAIL");

    // Member to hold the name
    private String string;

    // constructor to set the string
    TokenType(String token) {
	string = token;
    }

    @Override
    public String toString() {
	return string;
    }

}
