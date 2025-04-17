package it.andrea.start.security;

public interface EncrypterManager {

    String encode(CharSequence value);

    boolean matches(CharSequence rawPassword, String encodedPassword);

}
