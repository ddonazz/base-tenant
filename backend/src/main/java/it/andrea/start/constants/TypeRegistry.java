package it.andrea.start.constants;

import java.util.stream.Stream;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum TypeRegistry {

    BUSINESS("BUSINESS"), PRIVATE("PRIVATE");

    // Member to hold the name
    private String string;

    // constructor to set the string
    TypeRegistry(String name) {
	string = name;
    }

    @Override
    public String toString() {
	return string;
    }

    public static Stream<TypeRegistry> stream() {
	return Stream.of(TypeRegistry.values());
    }

}
