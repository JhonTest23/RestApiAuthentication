package co.com.pragmaauthen.r2dbc;

import co.com.pragmaauthen.model.user.User;
import co.com.pragmaauthen.model.user.gateways.UserRepository;
import co.com.pragmaauthen.r2dbc.entity.UserData;
import co.com.pragmaauthen.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.*;

import java.util.List;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User/* change for domain model */,
        UserData/* change for adapter model */,
        Integer,
    MyReactiveRepository
> implements UserRepository {
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, User.class/* change for domain model */));
    }

    @Transactional
    @Override
    public Mono<User> save(User user) {
        return super.save(user);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(this::toEntity);
    }

    @Override
    public Flux<User> findByEmails(List<String> emails) {
        return repository.findByEmails(emails).map(this::toEntity);
    }

    protected User toEntity(UserData data) {
        return User.builder()
                .id(data.getId())
                .documentoIdentidad(data.getDocumentoIdentidad())
                .nombres(data.getNombres())
                .apellidos(data.getApellidos())
                .fechaNacimiento(data.getFechaNacimiento())
                .direccion(data.getDireccion())
                .telefono(data.getTelefono())
                .email(data.getEmail().toLowerCase())
                .salarioBase(data.getSalarioBase())
                .idRol(data.getIdRol())
                .contrasena(data.getContrasena())
                .build();
    }

    protected UserData toData(User user) {
        return UserData.builder()
                .id(user.getId())
                .documentoIdentidad(user.getDocumentoIdentidad())
                .nombres(user.getNombres())
                .apellidos(user.getApellidos())
                .fechaNacimiento(user.getFechaNacimiento())
                .direccion(user.getDireccion())
                .telefono(user.getTelefono())
                .email(user.getEmail().toLowerCase())
                .salarioBase(user.getSalarioBase())
                .idRol(user.getIdRol())
                .contrasena(user.getContrasena())
                .build();
    }

}
