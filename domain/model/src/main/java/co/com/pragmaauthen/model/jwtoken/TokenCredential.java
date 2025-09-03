package co.com.pragmaauthen.model.jwtoken;
import co.com.pragmaauthen.model.role.Role;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TokenCredential {
    private String documentoIdentidad;
    private String role;
    private String email;
    private String fullname;
}
