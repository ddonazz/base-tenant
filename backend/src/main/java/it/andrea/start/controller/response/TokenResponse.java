package it.andrea.start.controller.response;

import java.io.Serializable;

public class TokenResponse implements Serializable {

    private static final long serialVersionUID = -1252821933964715766L;

    private String token;

    public TokenResponse() {
        super();
    }

    public TokenResponse(String token) {
        super();
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenResponse [token=" + this.token + "]";
    }

}
