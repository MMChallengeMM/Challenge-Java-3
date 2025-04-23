package challenge.fiap.resources;

import challenge.fiap.dtos.ExceptionResponse;
import challenge.fiap.dtos.PageResponse;
import challenge.fiap.dtos.SearchResponse;
import challenge.fiap.models.FAILURE_TYPE;
import challenge.fiap.models.Failure;
import challenge.fiap.repositories.FailureRepo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

@Path("/falhas")
public class FailureResource {

    private final FailureRepo REPO = new FailureRepo();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFailures(

            @QueryParam("page") @DefaultValue("1")
            int page,

            @QueryParam("size") @DefaultValue("20")
            int pageSize) {

        page = page <= 0 ? 1 : page;
        try {
            var failures = REPO.get().stream()
                    .sorted(Comparator.comparing(Failure::getGenerationDate))
                    .toList();

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
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFailuresFiltered(

            Optional<FAILURE_TYPE> type,

            Optional<Integer> startYear,

            Optional<Integer> endYear,

            @QueryParam("page") @DefaultValue("1")
            int page,

            @QueryParam("size") @DefaultValue("20")
            int pageSize,

            @DefaultValue("date")
            String orderBy,

            @DefaultValue("false")
            boolean ascending) {

        if (type.isEmpty() && startYear.isEmpty() && endYear.isEmpty() && orderBy.equals("date") && !ascending) {
            return getFailures(page, pageSize);
        } else if (type.isPresent() && Arrays.stream(FAILURE_TYPE.values()).noneMatch(ft -> ft == type.get())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ExceptionResponse("Tipo de falha inválida. Verifique se este tipo de falha existe."))
                    .build();
        } else if ((startYear.isPresent() && endYear.isPresent()) && (startYear.get() > endYear.get())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ExceptionResponse("Data inválida para filtro. Ano incial é maior que ano final."))
                    .build();
        }

        page = page <= 0 ? 1 : page;
        try {
            var failures = REPO.get().stream()
                    .filter(f ->
                            (type.isEmpty() || f.getFailureType().equals(type.get())) &&
                                    (startYear.isEmpty() || f.getGenerationDate().getYear() >= startYear.get()) &&
                                    (endYear.isEmpty() || f.getGenerationDate().getYear() <= endYear.get()))
                    .sorted(ascending ?
                            orderBy.equals("date") ?
                                    Comparator.comparing(Failure::getGenerationDate) :
                                    Comparator.comparing(Failure::getDescription)
                            :
                            orderBy.equals("date") ?
                                    Comparator.comparing(Failure::getGenerationDate).reversed() :
                                    Comparator.comparing(Failure::getDescription).reversed()
                    ).toList();

            var start = (page - 1) * pageSize;
            var end = Math.min(failures.size(), start + pageSize);

            var failuresPaginated = failures.subList(start, end);

            return Response.ok(
                    new SearchResponse<>(
                            new PageResponse<>(page, pageSize, failures.size(), failuresPaginated),
                            type.toString(),
                            startYear.orElse(null),
                            endYear.orElse(null),
                            orderBy,
                            ascending
                    )
            ).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ExceptionResponse(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFailureById(
            @PathParam("id")
            UUID id) {

        try {
            var failure = REPO.getById(id);

            return Response.ok(
                    failure
            ).build();

        } catch (IllegalArgumentException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(new ExceptionResponse(e.getMessage()))
                    .build();
        } catch (RuntimeException e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ExceptionResponse(e.getMessage()))
                    .build();
        }

    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFailure(

            @PathParam("id")
            UUID id,

            Failure newFailure) {

        try {
            var failure = REPO.getById(id);
            failure.updateAttributes(newFailure);
            REPO.updateById(id, failure);
            return Response.ok(
                    failure
            ).build();
        } catch (IllegalArgumentException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(new ExceptionResponse(e.getMessage()))
                    .build();
        } catch (RuntimeException e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ExceptionResponse(e.getMessage()))
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFailure(

            Failure failure) {

        try {
            var failureToAdd = new Failure(failure.getDescription(), failure.getFailureType());
            REPO.add(failureToAdd);
            return Response.ok(
                    failureToAdd
            ).build();
        } catch (RuntimeException e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ExceptionResponse(e.getMessage()))
                    .build();
        }
    }

}
