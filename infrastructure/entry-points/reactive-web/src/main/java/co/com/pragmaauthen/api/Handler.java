package co.com.pragmaauthen.api;

import co.com.pragmaauthen.api.Jwt.JwtUtil;
import co.com.pragmaauthen.model.jwtoken.JWToken;
import co.com.pragmaauthen.model.login.Login;
import co.com.pragmaauthen.model.user.User;
import co.com.pragmaauthen.usecase.login.LoginUseCase;
import co.com.pragmaauthen.usecase.user.UserUseCase;
//import co.com.pragmaauthen.usecase.userlog.UserLogUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    //    private final UserLogUseCase userLogUseCase;
    private final LoginUseCase loginUseCase;
    private final JwtUtil jwtUtil;


    public Mono<ServerResponse> saveUser(ServerRequest request) {
        log.info("Incoming request: {} {} with params {}", request.methodName(), request.path(), request.queryParams());
        return request.bodyToMono(User.class)
                .doOnNext(user -> log.debug("Request body: {}", user))
                .flatMap(userUseCase::saveUser)
                .doOnSuccess(saved -> log.info("User saved successfully: {}", saved.getEmail()))
                .doOnError(err -> log.error("Error saving user", err))
                .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).bodyValue(user))
                .onErrorResume(IllegalArgumentException.class, e -> {
                    log.warn("Conflict while saving user: {}", e.getMessage());
                    return ServerResponse.status(HttpStatus.CONFLICT).bodyValue(e.getMessage());
                });
    }

    public Mono<ServerResponse> TestUser(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(Mono.just("Bienvenido a la prueba"), String.class);
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        log.info("Incoming request: {} {} with params {}", request.methodName(), request.path(), request.queryParams());
        return request.bodyToMono(Login.class)
                .doOnNext(login -> log.debug("Login attempt for email: {}", login.getEmail()))
                .flatMap(loginUseCase::checkUser)
                .flatMap(loginUseCase::setCredentials)
                .flatMap(credential -> {

                    String token = jwtUtil.generateToken(
                            credential.getDocumentoIdentidad(),
                            credential.getRole(),
                            credential.getEmail(),
                            credential.getFullname()
                    );
                    log.info("JWT generated for email: {}", credential.getEmail());
                    return ServerResponse.ok().bodyValue(new JWToken(token, credential.getDocumentoIdentidad(), credential.getRole(), credential.getEmail()));
                })
                .doOnError(err -> log.error("Error during login process", err))
                .onErrorResume(IllegalArgumentException.class, e -> {
                    log.warn("Invalid login attempt: {}", e.getMessage());
                    return ServerResponse.status(HttpStatus.CONFLICT).bodyValue(e.getMessage());
                });
    }

    public Mono<ServerResponse> getUsersByEmails(ServerRequest request) {
        log.info("Fetching users by emails - {}", request.queryParams());
        return request.bodyToMono(List.class)
                .doOnNext(emails -> log.debug("Emails received: {}", emails))// Expect a JSON array of emails
                .flatMapMany(userUseCase::getUsersByEmails)
                .doOnComplete(() -> log.info("User info completed"))
                .doOnError(err -> log.error("Error with info completed", err))
                .collectList()
                .flatMap(users -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(users));
    }
}
