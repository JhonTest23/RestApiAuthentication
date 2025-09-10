package co.com.pragmaauthen.usecase.role;

import co.com.pragmaauthen.model.role.Role;
import co.com.pragmaauthen.model.role.gateways.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class RoleUseCaseTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleUseCase roleUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRoleById_success() {
        // Arrange
        Role role = Role.builder()
                .id(1)
                .nombre("ADMIN")
                .descripcion("Administrator role")
                .activo(true)
                .build();

        when(roleRepository.findRoleById(1)).thenReturn(Mono.just(role));

        // Act + Assert
        StepVerifier.create(roleUseCase.getRoleById(1))
                .expectNext(role)
                .verifyComplete();

        verify(roleRepository, times(1)).findRoleById(1);
    }

    @Test
    void getRoleById_notFound() {
        // Arrange
        when(roleRepository.findRoleById(99)).thenReturn(Mono.empty());

        // Act + Assert
        StepVerifier.create(roleUseCase.getRoleById(99))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Role not found")
                )
                .verify();

        verify(roleRepository, times(1)).findRoleById(99);
    }
}
