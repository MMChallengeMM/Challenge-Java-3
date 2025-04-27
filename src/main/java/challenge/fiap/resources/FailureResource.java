package challenge.fiap.resources;

import challenge.fiap.dtos.ExceptionDto;
import challenge.fiap.dtos.PageDto;
import challenge.fiap.dtos.SearchDto;
import challenge.fiap.models.FAILURE_TYPE;
import challenge.fiap.models.Failure;
import challenge.fiap.repositories.FailureRepo;
import challenge.fiap.service.FailureService;
import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;

@Path("/falhas")
public class FailureResource {

    private final FailureRepo REPO = new FailureRepo();

    @GET
    @RateLimit(value = 50)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFailures(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("20") int pageSize) {

        page = page <= 0 ? 1 : page;
        try {

            var failures = REPO.get().stream()
                    .sorted(Comparator.comparing(Failure::getGenerationDate))
                    .toList();

            var start = (page - 1) * pageSize;
            var end = Math.min(failures.size(), start + pageSize);

            var failuresPaginated = failures.subList(start, end);

            return Response.ok(
                    new PageDto<>(
                            page,
                            pageSize,
                            failures.size(),
                            failuresPaginated)
            ).build();

        } catch (RuntimeException e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ExceptionDto(e.toString(),
                            e.getMessage()))
                    .build();
        }
    }

    @GET
    @RateLimit(value = 300)
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFailuresFiltered(

            @QueryParam("type") Optional<FAILURE_TYPE> type,

            @QueryParam("from") Optional<Integer> startYear,

            @QueryParam("to") Optional<Integer> endYear,

            @QueryParam("page") @DefaultValue("1") int page,

            @QueryParam("size") @DefaultValue("20") int pageSize,

            @QueryParam("orderby") @DefaultValue("date") String orderBy,

            @QueryParam("ascending") @DefaultValue("false") boolean ascending) {

        if (type.isPresent() && Arrays.stream(FAILURE_TYPE.values()).noneMatch(ft -> ft == type.get())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ExceptionDto(new IllegalArgumentException("Tipo de falha inválido").toString(),
                            "Verifique se este tipo de falha está digitado corretamente ou existe."))
                    .build();
        } else if ((startYear.isPresent() && endYear.isPresent()) && (startYear.get() > endYear.get())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ExceptionDto(new IllegalArgumentException("Data inválida").toString(),
                            "Ano inicial é maior que ano final."))
                    .build();
        }

        page = page <= 0 ? 1 : page;
        try {
            var filters = new HashMap<String, Object>();
            type.ifPresent(ft -> filters.put("falhaTipo", ft));
            startYear.ifPresent(sy -> filters.put("anoInicial", sy));
            endYear.ifPresent(ey -> filters.put("anoFinal", ey));

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
                    new SearchDto<>(
                            new PageDto<>(page, pageSize, failures.size(), failuresPaginated),
                            filters,
                            orderBy,
                            ascending
                    )
            ).build();

        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ExceptionDto(e.toString(),
                            e.getMessage()))
                    .build();
        }
    }

    @GET
    @RateLimit
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFailureById(
            @PathParam("id") UUID id) {

        try {

            var failureOptional = REPO.getById(id);
            if (failureOptional.isPresent()) {

                return Response.ok(
                        failureOptional.get()
                ).build();

            } else {

                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(new ExceptionDto(new NotFoundException("Falha não encontrada").toString(),
                                "Verifique se o ID é válido."))
                        .build();

            }

        } catch (RuntimeException e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ExceptionDto(e.toString(),
                            e.getMessage()))
                    .build();
        }
    }

    @PUT
    @RateLimit(value = 50)
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFailure(
            @PathParam("id") UUID id,
            Failure newFailure) {

        try {

            var failureOptional = REPO.getById(id);
            Failure failure;
            if (failureOptional.isPresent()) {
                failure = failureOptional.get();
            } else {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(new ExceptionDto(new NotFoundException("Falha não encontrada").toString(),
                                "Verifique se o ID é válido."))
                        .build();
            }

            failure.updateAttributes(newFailure);
            REPO.updateById(id, failure);
            return Response.ok(
                    failure
            ).build();

        } catch (RuntimeException e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ExceptionDto(e.toString(),
                            e.getMessage()))
                    .build();
        }
    }

    @POST
    @RateLimit(value = 50)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFailure(Failure failure) {

        if (!FailureService.createFailureCheck(failure)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ExceptionDto(new IllegalArgumentException("Falha inválida").toString(),
                            "Verifique se o campo 'description' está preenchido corretamente"))
                    .build();
        }

        try {
            REPO.add(failure);
            return Response.ok(
                    failure
            ).build();

        } catch (RuntimeException e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ExceptionDto(e.toString(),
                            e.getMessage()))
                    .build();
        }
    }

}
