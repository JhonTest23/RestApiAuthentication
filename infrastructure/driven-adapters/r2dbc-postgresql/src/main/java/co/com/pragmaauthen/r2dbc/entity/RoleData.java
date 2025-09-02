package co.com.pragmaauthen.r2dbc.entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rol")
public class RoleData {
    @Id
    @Column("id_rol")
    private Integer id;
    @Column("nombre")
    private String nombre;
    private String descripcion;
    private Boolean activo;
}
