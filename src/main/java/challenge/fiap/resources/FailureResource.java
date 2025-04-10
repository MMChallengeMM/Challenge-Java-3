package challenge.fiap.resources;

import challenge.fiap.models.FAILURE_STATUS;
import challenge.fiap.models.FAILURE_TYPE;
import challenge.fiap.models.Failure;
import challenge.fiap.repositories.FailureRepo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/api/falhas")
public class FailureResource {

    private final FailureRepo REPO = new FailureRepo();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFailures() {
        var failures = REPO.get();
        //TODO: Paginação
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response failureById(@PathParam("id") UUID id) {
        var failure = REPO.getById(id);
        //TODO: Enviar a falha para o front
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @PUT
    @Path("/{id}/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateFailure(@PathParam("id") UUID id) {
        var failure = REPO.getById(id);

        //TODO: Capturar dados do front
        failure.setFailureStatus(FAILURE_STATUS.CANCELADA);

        REPO.updateById(id, failure);
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addFailure() {
        //TODO: Capturar dados do front
        var failure = new Failure("DESCRICAO", FAILURE_TYPE.ELETRICA);
        REPO.add(failure);
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

}
