package co.com.pragmaauthen.r2dbc.userLogBD;

import co.com.pragmaauthen.model.user.User;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userlog")
public class UserLogData {
    @Id
    private String id;
    private User user;
    private String action;   // e.g., LOGIN, LOGOUT, UPDATE
    private String timestamp;
}
