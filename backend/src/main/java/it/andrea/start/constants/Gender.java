package it.andrea.start.constants;

import java.util.stream.Stream;

public enum Gender {

    MALE("Maschio"),
    FEMALE("Femmina"),
    NOT_SPECIFIED("Non specificato");

    private String string;

    Gender(String name) {
        string = name;
    }

    public static Stream<Gender> stream() {
        return Stream.of(Gender.values());
    }

    @Override
    public String toString() {
        return string;
    }

}
