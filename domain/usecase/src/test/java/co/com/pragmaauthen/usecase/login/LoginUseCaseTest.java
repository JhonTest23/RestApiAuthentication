package co.com.pragmaauthen.usecase.login;

import co.com.pragmaauthen.model.jwtoken.gateways.PasswordEncrypter;
import co.com.pragmaauthen.model.user.User;
import co.com.pragmaauthen.model.role.Role;
import co.com.pragmaauthen.model.login.Login;
import co.com.pragmaauthen.model.user.gateways.UserRepository;
import co.com.pragmaauthen.model.role.gateways.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class LoginUseCaseTest {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncrypter passwordEncrypter;
    private LoginUseCase loginUseCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        passwordEncrypter = mock(PasswordEncrypter.class);
        loginUseCase = new LoginUseCase(userRepository, roleRepository, passwordEncrypter);
    }

    @Test
    void checkUser_success() {
        Login login = Login.builder().email("correo1@correo.com").password("1234").build();
        User user = User.builder()
                .id(1)
                .email("correo1@correo.com")
                .contrasena("1234")
                .nombres("Miguel")
                .apellidos("Rodriguez")
                .idRol(1)
                .documentoIdentidad("123456")
                .build();

        when(userRepository.findByEmail("correo1@correo.com")).thenReturn(Mono.just(user));

        StepVerifier.create(loginUseCase.checkUser(login))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository, times(1)).findByEmail("correo1@correo.com");
    }

    @Test
    void checkUser_userNotFound() {
        Login login = Login.builder().email("emailrandom@correo.com").password("1234").build();

        when(userRepository.findByEmail("emailrandom@correo.com")).thenReturn(Mono.empty());

        StepVerifier.create(loginUseCase.checkUser(login))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("User not found"))
                .verify();
    }

    @Test
    void checkUser_invalidPassword() {
        Login login = Login.builder().email("emailrandom@correo.com").password("wrong").build();
        User user = User.builder()
                .id(1)
                .email("emailrandom@correo.com")
                .contrasena("1234")
                .build();

        when(userRepository.findByEmail("emailrandom@correo.com")).thenReturn(Mono.just(user));

        StepVerifier.create(loginUseCase.checkUser(login))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Invalid password"))
                .verify();
    }

    @Test
    void setCredentials_success() {
        User user = User.builder()
                .id(1)
                .email("emailfound@coreo.com")
                .nombres("Juan")
                .apellidos("Perez")
                .idRol(1)
                .documentoIdentidad("123456")
                .build();

        Role role = Role.builder().id(1).nombre("ADMIN").build();

        when(roleRepository.findRoleById(1)).thenReturn(Mono.just(role));

        StepVerifier.create(loginUseCase.setCredentials(user))
                .expectNextMatches(token ->
                        token.getEmail().equals("emailfound@coreo.com") &&
                                token.getRole().equals("ADMIN") &&
                                token.getFullname().equals("Juan Perez")
                )
                .verifyComplete();
    }

    @Test
    void setCredentials_roleNotFound() {
        User user = User.builder().idRol(99).build();

        when(roleRepository.findRoleById(99)).thenReturn(Mono.empty());

        StepVerifier.create(loginUseCase.setCredentials(user))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("The role doesn't exist"))
                .verify();
    }
}
