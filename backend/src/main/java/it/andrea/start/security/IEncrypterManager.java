package it.andrea.start.security;

public interface IEncrypterManager {

    public String encode(CharSequence value);

    public boolean matches(CharSequence rawPassword, String encodedPassword);

}
