package co.com.pragmaauthen.r2dbc;

import co.com.pragmaauthen.model.role.Role;
import co.com.pragmaauthen.model.role.gateways.RoleRepository;
import co.com.pragmaauthen.r2dbc.entity.RoleData;
import co.com.pragmaauthen.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class RoleReactiveRepositoryAdapter  extends ReactiveAdapterOperations<
        Role/* change for domain model */,
        RoleData/* change for adapter model */,
        Integer,
        RoleReactiveRepository
        > implements RoleRepository {
    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Role.class/* change for domain model */));
    }

    @Override
    public Mono<Role> findRoleById(Integer idRole) {
        return repository.findRoleById(idRole)
                .map(this::toEntity);
    }

    protected Role toEntity(RoleData data) {
        return Role.builder()
                .id(data.getId())
                .nombre(data.getNombre())
                .descripcion(data.getDescripcion())
                .activo(data.getActivo())
                .build();
    }
}