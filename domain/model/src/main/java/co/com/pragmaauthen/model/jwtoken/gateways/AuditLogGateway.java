package co.com.pragmaauthen.model.jwtoken.gateways;

import reactor.core.publisher.Mono;

public interface AuditLogGateway {
    Mono<Void> logStateChange(Integer requestId, Integer newState);
}
