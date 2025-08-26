package co.com.pragmaauthen.model.userlog;
import co.com.pragmaauthen.model.user.User;
import lombok.*;

import java.time.LocalDateTime;

//@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLog {
    private Integer id;
    private User user;
    private String action;   // e.g., LOGIN, LOGOUT, UPDATE
    private String timestamp;

    public UserLog(User user, String action, String timestamp) {
        this.user = user;
        this.action = action;
        this.timestamp = timestamp;
    }
}
