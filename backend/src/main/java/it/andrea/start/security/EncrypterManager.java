package it.andrea.start.security;

public interface EncrypterManager {

    public String encode(CharSequence value);

    public boolean matches(CharSequence rawPassword, String encodedPassword);

}
