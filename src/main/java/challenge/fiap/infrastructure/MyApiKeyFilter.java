package challenge.fiap.infrastructure;

import challenge.fiap.dtos.ExceptionResponse;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class MyApiKeyFilter implements ContainerRequestFilter {

    @ConfigProperty(name="api.key")
    String apiKey;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        var apiKeyRequest = containerRequestContext.getHeaderString("X-API-key");

        if (apiKeyRequest == null || !apiKeyRequest.equals(this.apiKey)) {
            containerRequestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity(new ExceptionResponse("Chave inválida"))
                            .build()
            );
        }
    }
}
