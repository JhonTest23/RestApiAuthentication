package co.com.pragmaauthen.r2dbc;

import co.com.pragmaauthen.model.user.User;
import co.com.pragmaauthen.r2dbc.entity.UserData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

// TODO: This file is just an example, you should delete or modify it
public interface MyReactiveRepository extends ReactiveCrudRepository<UserData, Integer>, ReactiveQueryByExampleExecutor<UserData> {
    Mono<UserData> findByEmail(String email);
    @Query("SELECT * FROM users WHERE correo_electronico IN (:emails)")
    Flux<UserData> findByEmails(List<String> emails);
}
