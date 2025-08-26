package co.com.pragmaauthen.r2dbc.userLogBD;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserLogReactiveRepository extends ReactiveCrudRepository<UserLogData, String>, ReactiveQueryByExampleExecutor<UserLogData> {
}
