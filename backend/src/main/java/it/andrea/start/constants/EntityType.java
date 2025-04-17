package it.andrea.start.constants;

public enum EntityType {

    SYSTEM("SYSTEM"),
    ADMIN("ADMIN");

    private final String value;

    EntityType(String value) {
        this.value = value;
    }

    public static EntityType fromValue(String value) {
        for (EntityType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Valore non valido per EntityType: " + value);
    }

    public String getValue() {
        return value;
    }

}
