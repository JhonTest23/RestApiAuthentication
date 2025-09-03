package co.com.pragmaauthen.usecase.user;

import co.com.pragmaauthen.model.user.User;
import co.com.pragmaauthen.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;

//    public Mono<User> saveUser(User user) {
//        User userNormalized = normalizedEmail(user);
//
//        return userRepository.findByEmail(userNormalized.getEmail())
//                .flatMap(existing -> Mono.<User>error(new IllegalArgumentException("Email already exists")))
//                .switchIfEmpty(userRepository.save(userNormalized));
//    }

    public Mono<User> saveUser(User user) {
        User userNormalized = normalizedEmail(user);

        return userRepository.findByEmail(userNormalized.getEmail())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("Email already exists"));
                    } else {
                        return userRepository.save(userNormalized);
                    }
                });
    }

    private User normalizedEmail(User user){
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
