package co.com.pragmaauthen.r2dbc.userLogBD;

import co.com.pragmaauthen.model.user.User;
import co.com.pragmaauthen.model.userlog.UserLog;
import co.com.pragmaauthen.model.userlog.gateways.UserLogRepository;
import co.com.pragmaauthen.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Repository
public class UserLogReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        UserLog/* change for domain model */,
        UserLogData/* change for adapter model */,
        String,
        UserLogReactiveRepository
        > implements UserLogRepository {
    public UserLogReactiveRepositoryAdapter(UserLogReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, UserLog.class/* change for domain model */));
    }

    @Transactional
    @Override
    public Mono<UserLog> saveLog(UserLog userLog) {
        return super.save(userLog);
    }

}