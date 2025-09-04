package co.com.pragmaauthen.model.jwtoken;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class JWToken {
    private String token;
    private String documentoIdentidad;
    private String rol;
    private String email;
}
