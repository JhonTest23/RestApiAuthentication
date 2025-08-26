package co.com.pragmaauthen.model.userlog.gateways;

import co.com.pragmaauthen.model.userlog.UserLog;
import reactor.core.publisher.Mono;

public interface UserLogRepository {
    Mono<UserLog> saveLog(UserLog userLog);
}
