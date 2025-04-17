package it.andrea.start.constants;

import java.util.stream.Stream;

public enum CustomerStatus {

    PENDING("In attesa"),
    ACTIVE("Attivo"),
    SUSPENDED("Sospeso"),
    CANCELLED("Cancellato"),
    BLACKLIST("In blacklist");

    private final String status;

    CustomerStatus(String name) {
        status = name;
    }

    public static Stream<CustomerStatus> stream() {
        return Stream.of(CustomerStatus.values());
    }

    @Override
    public String toString() {
        return status;
    }

}
