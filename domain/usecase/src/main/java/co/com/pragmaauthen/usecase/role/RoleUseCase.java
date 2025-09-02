package co.com.pragmaauthen.usecase.role;

import co.com.pragmaauthen.model.role.Role;
import co.com.pragmaauthen.model.role.gateways.RoleRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RoleUseCase {
    private final RoleRepository roleRepository;

    public Mono<Role> getRoleById(Integer id) {
        return roleRepository.findRoleById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Role not found")));
    }
}
