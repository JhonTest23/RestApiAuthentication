package co.com.pragmaauthen.usecase.user;

import co.com.pragmaauthen.model.user.User;
import co.com.pragmaauthen.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import lombok.*;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        return userRepository.save(user);
    }
}
