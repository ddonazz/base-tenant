package it.andrea.start.constants;

import java.util.stream.Stream;

public enum TypeRegistry {

    BUSINESS("BUSINESS"),
    PRIVATE("PRIVATE");

    private String string;

    TypeRegistry(String name) {
        string = name;
    }

    public static Stream<TypeRegistry> stream() {
        return Stream.of(TypeRegistry.values());
    }

    @Override
    public String toString() {
        return string;
    }

}
