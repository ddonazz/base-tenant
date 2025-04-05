package it.andrea.start.constants;

import java.util.stream.Stream;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum UnitTime {

    SECOND("SECOND"), MINUTE("MINUTE"), HOUR("HOUR"), DAY("DAY"), WEEK("WEEK"), MONTH("MONTH"), YEAR("YEAR");

    // Member to hold the name
    private String string;

    // constructor to set the string
    UnitTime(String name) {
	string = name;
    }

    @Override
    public String toString() {
	return string;
    }

    public static Stream<UnitTime> stream() {
	return Stream.of(UnitTime.values());
    }

}
