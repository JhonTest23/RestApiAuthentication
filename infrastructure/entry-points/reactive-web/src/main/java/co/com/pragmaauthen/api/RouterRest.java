package co.com.pragmaauthen.api;

import co.com.pragmaauthen.model.login.Login;
import co.com.pragmaauthen.model.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",     // <-- link to the actual route
                    method = RequestMethod.POST,              // <-- HTTP method
                    beanClass = Handler.class,                // <-- your handler class
                    beanMethod = "saveUser",
                    operation = @Operation(
                            operationId = "saveUser",
                            summary = "Save an user",
                            tags = {"Users"},
//                            security = {
//                                    @SecurityRequirement(name = "bearerAuth")  // <-- 🔑 Requires Bearer Token
//                            },
                            requestBody = @RequestBody(      // <-- here is the magic
                                    description = "JSON body with user data",
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = User.class)
                                    )
                            ),
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "user", description = "User")},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Handler.class))),
                                    @ApiResponse(responseCode = "500", description = "Invalid user")
                            }
                    )
            ),
            @RouterOperation(path = "/api/v3/usuarios", beanClass = Handler.class, beanMethod = "TestUser")
    })
    public RouterFunction<ServerResponse> userRoutes(Handler handler) {
        return route(POST("/api/v1/usuarios"), handler::saveUser)
                .andRoute(GET("/api/v3/usuarios"), handler::TestUser);
    }

    @Bean
    @RouterOperation(
            path = "/api/v1/login",     // <-- link to the actual route
            method = RequestMethod.POST,              // <-- HTTP method
            beanClass = Handler.class,                // <-- your handler class
            beanMethod = "login",
            operation = @Operation(
                    operationId = "login",
                    summary = "login",
                    tags = {"Auth"},
                    requestBody = @RequestBody(      // <-- here is the magic
                            description = "JSON body with login data",
                            required = true,
                            content = @Content(
                                    schema = @Schema(implementation = Login.class)
                            )
                    ),
                    parameters = {@Parameter(in = ParameterIn.PATH, name = "login", description = "Login")},
                    responses = {
                            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Handler.class))),
                            @ApiResponse(responseCode = "500", description = "Invalid user")
                    }
            )
    )
    public RouterFunction<ServerResponse> authRoutes(Handler handler) {
        return route(POST("/api/v1/login"), handler::login);
    }
}
