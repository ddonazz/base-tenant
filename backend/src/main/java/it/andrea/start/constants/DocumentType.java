package it.andrea.start.constants;

public enum DocumentType {

    IDENTITY_CARD("Carta d'identità"),
    PASSPORT("Passaporto"),
    DRIVER_LICENSE("Patente di guida"),
    BUSINESS_LICENSE("Licenza aziendale"),
    OTHER("Altro");

    private final String description;

    DocumentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
