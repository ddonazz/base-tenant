package it.andrea.start.error.exception.crypto;

public class CryptoOperationException extends Exception {
    private static final long serialVersionUID = 6246425692257776531L;

    public CryptoOperationException(String message) {
        super(message);
    }

    public CryptoOperationException(String message, Throwable cause) {
        super(message, cause);
    }

}
