package challenge.fiap.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

@Path("/falhas")
public class FailureResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String allFailures()  {
        return "";
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String failureById(@PathParam("id") UUID id) {
        return "";
    }
    
}
