package challenge.fiap.resources;

import challenge.fiap.dtos.ExceptionResponse;
import challenge.fiap.dtos.PageResponse;
import challenge.fiap.models.Failure;
import challenge.fiap.repositories.FailureRepo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.UUID;

@Path("/falhas")
public class FailureResource {

    private final FailureRepo REPO = new FailureRepo();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFailures(@QueryParam("page") @DefaultValue("1") int page, @QueryParam("size") @DefaultValue("20") int pageSize) {
        try {
            var failures = REPO.get();

            var start = (page - 1) * pageSize;
            var end = Math.min(failures.size(), start + pageSize);

            var failuresPaginated = failures.subList(start, end);

            return Response.ok(
                    new PageResponse<>(
                            page,
                            pageSize,
                            failures.size(),
                            failuresPaginated)
            ).build();
        } catch (RuntimeException e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ExceptionResponse(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFailureById(@PathParam("id") UUID id) {
        var failure = REPO.getById(id);
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateFailure(@PathParam("id") UUID id, Failure newFailure) {
        var failure = REPO.getById(id);

        //TODO: Capturar dados do front
        failure.updateAttributes(newFailure);

        REPO.updateById(id, failure);
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addFailure(Failure failure) {
        var failureToAdd = new Failure(failure.getDescription(), failure.getFailureType());
        REPO.add(failureToAdd);
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

}
