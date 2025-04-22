package challenge.fiap.resources;

import challenge.fiap.models.Report;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/relatorios")
public class ReportResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFailures() {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReportById(@PathParam("id") UUID id) {

    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateById(@PathParam("id") UUID id, Report report) {
        return null;
    }

}
