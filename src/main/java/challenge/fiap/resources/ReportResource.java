package challenge.fiap.resources;

import challenge.fiap.dtos.ExceptionResponse;
import challenge.fiap.dtos.PageResponse;
import challenge.fiap.models.REPORT_TYPE;
import challenge.fiap.models.Report;
import challenge.fiap.repositories.ReportRepo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Year;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

@Path("/relatorios")
public class ReportResource {

    private final ReportRepo REPO = new ReportRepo();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReports(

            @QueryParam("page") @DefaultValue("1")
            int page,

            @QueryParam("size") @DefaultValue("20")
            int pageSize
    ) {

        page = page <= 0 ? 1 : page;
        try {
            var reports = REPO.get().stream()
                    .sorted(Comparator.comparing(Report::getGenerationDate))
                    .toList();

            var start = (page - 1) * pageSize;
            var end = Math.min(reports.size(), start + pageSize);

            var reportsPaginated = reports.subList(start, end);

            return Response.ok(
                    new PageResponse<>(
                            page,
                            pageSize,
                            reports.size(),
                            reportsPaginated)
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
    public Response getReportsFiltered(

            @QueryParam("text")
            Optional<String> text,

            @QueryParam("type")
            Optional<REPORT_TYPE> type,

            @QueryParam("generationStartYear")
            Optional<Integer> generationStartYear,

            @QueryParam("generationEndYear")
            Optional<Integer> generationEndYear,

            @QueryParam("periodStartYear")
            Optional<Integer> periodStartYear,

            @QueryParam("periodEndYear")
            Optional<Integer> periodEndYear,

            @QueryParam("numberFailures")
            Optional<Integer> numFailures,

            @QueryParam("page") @DefaultValue("1")
            int page,

            @QueryParam("size") @DefaultValue("20")
            int pageSize,

            @QueryParam("orderby") @DefaultValue("date")
            String orderBy,

            @QueryParam("ascending") @DefaultValue("false")
            boolean ascending
    ) {

        return null;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReportById(@PathParam("id") UUID id) {
        return null;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateById(@PathParam("id") UUID id, Report report) {
        return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReport(Report report) {
        return null;
    }

}
