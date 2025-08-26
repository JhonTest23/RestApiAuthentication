package co.com.pragmaauthen.model.user.gateways;

import co.com.pragmaauthen.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> save(User user);
}
