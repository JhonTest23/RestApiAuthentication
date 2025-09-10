package co.com.pragmaauthen.usecase.user;

import co.com.pragmaauthen.model.jwtoken.TokenCredential;
import co.com.pragmaauthen.model.jwtoken.gateways.PasswordEncrypter;
import co.com.pragmaauthen.model.login.Login;
import co.com.pragmaauthen.model.role.Role;
import co.com.pragmaauthen.model.role.gateways.RoleRepository;
import co.com.pragmaauthen.model.user.User;
import co.com.pragmaauthen.model.user.gateways.UserRepository;
import co.com.pragmaauthen.usecase.login.LoginUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserUseCaseTest {
    private UserRepository userRepository;
    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userUseCase = new UserUseCase(userRepository);
    }

    @Test
    void saveUser_success() {
        // Arrange
        User user = User.builder().email("TEST@MAIL.COM").nombres("John").build();
        User normalized = User.builder().email("test@mail.com").nombres("John").build();

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Mono.empty());
        when(userRepository.save(argThat(u -> u.getEmail().equals("test@mail.com"))))
                .thenReturn(Mono.just(normalized));

        // Act + Assert
        StepVerifier.create(userUseCase.saveUser(user))
                .expectNextMatches(saved ->
                        saved.getEmail().equals("test@mail.com") &&
                                saved.getNombres().equals("John"))
                .verifyComplete();

        verify(userRepository, times(1)).findByEmail("test@mail.com");
        verify(userRepository, times(1)).save(normalized);
    }

    @Test
    void saveUser_emailAlreadyExists() {
        // Arrange
        User user = User.builder().email("TEST@MAIL.COM").nombres("John").build();
        User existing = User.builder().email("test@mail.com").build();

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Mono.just(existing));

        // Act + Assert
        StepVerifier.create(userUseCase.saveUser(user))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("Email already exists"))
                .verify();

        verify(userRepository, times(1)).findByEmail("test@mail.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUsersByEmails_success() {
        // Arrange
        List<String> emails = Arrays.asList("a@mail.com", "b@mail.com");
        User user1 = User.builder().email("a@mail.com").nombres("Alice").build();
        User user2 = User.builder().email("b@mail.com").nombres("Bob").build();

        when(userRepository.findByEmails(emails)).thenReturn(Flux.just(user1, user2));

        // Act + Assert
        StepVerifier.create(userUseCase.getUsersByEmails(emails))
                .expectNext(user1)
                .expectNext(user2)
                .verifyComplete();

        verify(userRepository, times(1)).findByEmails(emails);
    }

}
