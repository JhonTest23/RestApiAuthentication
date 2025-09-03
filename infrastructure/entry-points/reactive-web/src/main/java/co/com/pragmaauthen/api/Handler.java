package co.com.pragmaauthen.api;

import co.com.pragmaauthen.api.Jwt.JwtUtil;
import co.com.pragmaauthen.model.jwtoken.JWToken;
import co.com.pragmaauthen.model.login.Login;
import co.com.pragmaauthen.model.user.User;
import co.com.pragmaauthen.usecase.login.LoginUseCase;
import co.com.pragmaauthen.usecase.user.UserUseCase;
import co.com.pragmaauthen.usecase.userlog.UserLogUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserLogUseCase userLogUseCase;
    private final LoginUseCase loginUseCase;
    private final JwtUtil jwtUtil;


    public Mono<ServerResponse> saveUser(ServerRequest request) {
            return request.bodyToMono(User.class)
                .flatMap(userUseCase::saveUser)
                .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).bodyValue(user))
                .onErrorResume(IllegalArgumentException.class, e ->
                    ServerResponse.status(HttpStatus.CONFLICT).bodyValue(e.getMessage())
                );
    }

    public  Mono<ServerResponse> TestUser(ServerRequest request){
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(Mono.just("Bienvenido a la prueba"), String.class);
    }

    public Mono<ServerResponse> login(ServerRequest request) {

        return request.bodyToMono(Login.class)
                .flatMap(loginUseCase::checkUser)
                .flatMap(loginUseCase::setCredentials)
                .flatMap(credential -> {

                    String token = jwtUtil.generateToken(
                            credential.getDocumentoIdentidad(),
                            credential.getRole(),
                            credential.getEmail(),
                            credential.getFullname()
                    );
                    return ServerResponse.ok().bodyValue(new JWToken(token,credential.getDocumentoIdentidad(),credential.getRole()));
                })
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.status(HttpStatus.CONFLICT).bodyValue(e.getMessage())
                );
    }
}
