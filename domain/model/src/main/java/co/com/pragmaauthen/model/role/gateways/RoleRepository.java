package co.com.pragmaauthen.model.role.gateways;

import co.com.pragmaauthen.model.role.Role;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Role> findRoleById(Integer id);
}
