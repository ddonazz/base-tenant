package it.andrea.start.constants;

import java.util.stream.Stream;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum MessageType {

    MESSAGE_DATA("MESSAGE_DATA"), MESSAGE_GENERIC("MESSAGE_GENERIC"), MESSAGE_ERROR("MESSAGE_ERROR"), MESSAGE_SUCCESS("MESSAGE_SUCCESS"), MESSAGE_WARNING("MESSAGE_WARNING"), MESSAGE_INFO("MESSAGE_INFO");

    // Member to hold the name
    private String string;

    // constructor to set the string
    MessageType(String messageType) {
	string = messageType;
    }

    @Override
    public String toString() {
	return string;
    }

    public static Stream<MessageType> stream() {
	return Stream.of(MessageType.values());
    }

}
