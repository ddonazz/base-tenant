package it.andrea.start.constants;

import java.util.stream.Stream;

public enum UserStatus {

    PENDING("In attesa"),
    ACTIVE("Attivo"),
    SUSPENDED("Sospeso"),
    DEACTIVATE("Disattivato"),
    BLACKLIST("In blacklist"),
    LOCKED("Bloccato"),
    EXPIRED("Scaduto");

    private final String status;

    UserStatus(String name) {
        status = name;
    }

    public static Stream<UserStatus> stream() {
        return Stream.of(UserStatus.values());
    }

    @Override
    public String toString() {
        return status;
    }

}
