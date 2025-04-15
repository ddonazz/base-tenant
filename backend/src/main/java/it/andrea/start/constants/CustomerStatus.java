package it.andrea.start.constants;

import java.util.stream.Stream;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum CustomerStatus {

    PENDING("In attesa"), //
    ACTIVE("Attivo"), //
    SUSPENDED("Sospeso"), //
    CANCELLED("Cancellato"), //
    BLACKLIST("In blacklist");

    private String status;

    CustomerStatus(String name) {
        status = name;
    }

    @Override
    public String toString() {
        return status;
    }

    public static Stream<CustomerStatus> stream() {
        return Stream.of(CustomerStatus.values());
    }

}
