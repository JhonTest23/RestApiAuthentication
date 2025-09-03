package co.com.pragmaauthen.usecase.user;

import co.com.pragmaauthen.model.user.User;
import co.com.pragmaauthen.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserUseCaseTest {
    private UserRepository userRepository;
    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class); // mock infra dependency
        userUseCase = new UserUseCase(userRepository);       // inject mock
    }

    @Test
    void saveUser_shouldSaveWhenEmailDoesNotExist() {
        // Arrange
        User newUser = new User();
        newUser.setEmail("TEST@MAIL.COM");

        User savedUser = new User();
        savedUser.setEmail("test@mail.com");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Mono.empty()); // no user with same email
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(savedUser));

        // Act
        Mono<User> result = userUseCase.saveUser(newUser);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(user -> user.getEmail().equals("test@mail.com")) // normalized
                .verifyComplete();

        verify(userRepository).findByEmail("test@mail.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void saveUser_shouldThrowErrorWhenEmailAlreadyExists() {
        // Arrange
        User existingUser = new User();
        existingUser.setEmail("test@mail.com");

        User newUser = new User();
        newUser.setEmail("test@mail.com");

        // 👉 Mock repository to return a user (duplicate found)
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Mono.just(existingUser));

        // Act
        Mono<User> result = userUseCase.saveUser(newUser);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Email already exists")
                )
                .verify();

        // And verify save was never called
        verify(userRepository, never()).save(any(User.class));
    }
}
