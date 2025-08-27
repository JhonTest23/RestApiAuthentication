package co.com.pragmaauthen.model.user;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private  String nombres;
    private  String apellidos;
    private String fechaNacimiento;
    private  String direccion;
    private  Integer telefono;
    private  String email;
    private  double salarioBase;
}
