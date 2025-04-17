package it.andrea.start.controller.response;

import java.io.Serial;
import java.io.Serializable;

public class TokenResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -1252821933964715766L;

    private final String token;

    public TokenResponse(String token) {
        super();
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenResponse [token=" + this.token + "]";
    }

}
