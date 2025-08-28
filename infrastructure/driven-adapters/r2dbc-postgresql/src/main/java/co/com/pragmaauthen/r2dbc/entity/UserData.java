package co.com.pragmaauthen.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users") // your PostgreSQL table name
public class UserData {
    @Id
    @Column("id_usuario")
    private Integer id;
    private  String nombres;
    private  String apellidos;
    @Column("fecha_nacimiento")
    private String fechaNacimiento;
    private  String direccion;
    private  Integer telefono;
    @Email(message = "Correo electrónico inválido")
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Column("correo_electronico")
    private  String email;
    @DecimalMin(value = "0.0", inclusive = true, message = "El salario no puede ser menor que 0")
    @DecimalMax(value = "15000000.0", inclusive = true, message = "El salario no puede ser mayor a 15,000,000")
    @Column("salario_base")
    private  double salarioBase;
}