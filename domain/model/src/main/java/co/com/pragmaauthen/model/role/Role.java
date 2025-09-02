package co.com.pragmaauthen.model.role;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Role {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Boolean activo;
}
