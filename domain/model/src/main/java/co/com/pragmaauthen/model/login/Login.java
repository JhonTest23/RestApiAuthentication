package co.com.pragmaauthen.model.login;
import lombok.*;
//import lombok.NoArgsConstructor;


@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    private String email;
    private String password;
}
