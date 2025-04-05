package it.andrea.start.constants;

import java.util.stream.Stream;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum MediaType {

    IMAGE("IMAGE"), IMAGE_MAP("MAP IMAGE"), VIDEO("VIDEO"), DOCUMENT("DOCUMENT"), TEXT_FILE("TEXT FILE"), BINARY_FILE("BINARY FILE");

    // Member to hold the name
    private String string;

    // constructor to set the string
    MediaType(String name) {
	string = name;
    }

    @Override
    public String toString() {
	return string;
    }

    public static Stream<MediaType> stream() {
	return Stream.of(MediaType.values());
    }

}
