package challenge.fiap.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/users")
public class UserResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response allUsers() {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
}