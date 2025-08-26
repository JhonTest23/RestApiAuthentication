package co.com.pragmaauthen.usecase.userlog;

import co.com.pragmaauthen.model.user.User;
import co.com.pragmaauthen.model.userlog.UserLog;
import co.com.pragmaauthen.model.userlog.gateways.UserLogRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class UserLogUseCase {
    private UserLogRepository userLogRepository;

    public UserLogUseCase(UserLogRepository userLogRepository) {
        this.userLogRepository = userLogRepository;
    }

    public Mono<UserLog> saveUserLog(User user) {
        UserLog userLog = new UserLog(user, "Create user", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        return userLogRepository.saveLog(userLog);
    }
}
