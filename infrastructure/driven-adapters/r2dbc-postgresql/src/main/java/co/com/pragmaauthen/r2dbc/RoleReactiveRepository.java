package co.com.pragmaauthen.r2dbc;

import co.com.pragmaauthen.r2dbc.entity.RoleData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RoleReactiveRepository  extends ReactiveCrudRepository<RoleData, Integer>, ReactiveQueryByExampleExecutor<RoleData> {
    Mono<RoleData> findRoleById(Integer id);
}