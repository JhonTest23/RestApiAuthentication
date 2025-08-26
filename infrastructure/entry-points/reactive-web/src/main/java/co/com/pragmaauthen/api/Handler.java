package co.com.pragmaauthen.api;

import co.com.pragmaauthen.model.user.User;
import co.com.pragmaauthen.usecase.user.UserUseCase;
import co.com.pragmaauthen.usecase.userlog.UserLogUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserLogUseCase userLogUseCase;

    public Mono<ServerResponse> saveUser(ServerRequest request) {
        return request.bodyToMono(User.class)
                .flatMap(userUseCase::saveUser)
//                .flatMap(userLogUseCase::saveUserLog)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user));
    }

    public  Mono<ServerResponse> TestUser(ServerRequest request){
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(Mono.just("Bienvenido a la prueba"), String.class);
    }
}
