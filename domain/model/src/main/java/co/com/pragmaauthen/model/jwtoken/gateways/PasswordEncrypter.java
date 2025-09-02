package co.com.pragmaauthen.model.jwtoken.gateways;

public interface PasswordEncrypter {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}