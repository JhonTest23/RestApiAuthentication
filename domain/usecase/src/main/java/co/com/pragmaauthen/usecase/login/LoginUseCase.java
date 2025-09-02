package co.com.pragmaauthen.usecase.login;

import co.com.pragmaauthen.model.jwtoken.TokenCredential;
import co.com.pragmaauthen.model.jwtoken.gateways.PasswordEncrypter;
import co.com.pragmaauthen.model.login.Login;
import co.com.pragmaauthen.model.role.Role;
import co.com.pragmaauthen.model.role.gateways.RoleRepository;
import co.com.pragmaauthen.model.user.User;
import co.com.pragmaauthen.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncrypter passwordEncrypter;

    public Mono<User> checkUser(Login login) {
        return userRepository.findByEmail(login.getEmail())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("User not found")))
                .flatMap(user -> {
                    if (login.getPassword().equals(user.getContrasena())) {
                        return Mono.just(user); // ✅ authenticated
                    } else {
                        return Mono.error(new IllegalArgumentException("Invalid password"));
                    }
                });
    }

    public Mono<TokenCredential> setCredentials(User user) {
        return roleRepository.findRoleById(user.getIdRol())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("The role doesn't exist")))
                .map(role -> TokenCredential.builder()
                        .documentoIdentidad(user.getDocumentoIdentidad()) // lo metes en el token
                        .role(role.getNombre())                          // también el rol
                        .build()
                );
    }
}
