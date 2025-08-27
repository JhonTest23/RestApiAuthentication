package co.com.pragmaauthen.usecase.user;

import co.com.pragmaauthen.model.user.User;
import co.com.pragmaauthen.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import lombok.*;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;

//    public Mono<User> saveUser(User user) {
//        return userRepository.save(user);
//    }

    public Mono<User> saveUser(User user) {
        // normalize email to lowercase
        String normalizedEmail = user.getEmail().toLowerCase();

        // rebuild the user with normalized email
        User userNormalized = user.toBuilder()
                .email(normalizedEmail)
                .build();

        return userRepository.findByEmail(user.getEmail())
                .flatMap(existing -> Mono.<User>error(new IllegalArgumentException("Email already exists")))
                .switchIfEmpty(userRepository.save(user));
    }
}
