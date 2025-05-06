package challenge.fiap.resources;

import challenge.fiap.dtos.ExceptionDto;
import challenge.fiap.dtos.PageDto;
import challenge.fiap.dtos.SearchDto;
import challenge.fiap.models.REPORT_TYPE;
import challenge.fiap.models.Report;
import challenge.fiap.repositories.ReportRepo;
import challenge.fiap.service.ReportService;
import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;

@Path("/relatorios")
public class ReportResource {

    private final ReportRepo REPO = new ReportRepo();

    @GET
    @RateLimit(value=50)
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
                    new PageDto<>(
                            page,
                            pageSize,
                            reports.size(),
                            reportsPaginated)
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
    public Response getReportsFiltered(

            @QueryParam("text")
            Optional<String> text,

            @QueryParam("type")
            Optional<REPORT_TYPE> type,

            @QueryParam("generationFrom")
            Optional<Integer> generationStartYear,

            @QueryParam("generationTo")
            Optional<Integer> generationEndYear,

            @QueryParam("periodFrom")
            Optional<Integer> periodStartYear,

            @QueryParam("periodTo")
            Optional<Integer> periodEndYear,

            @QueryParam("failuresOnUpTo")
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

        if (text.isPresent() && text.get().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ExceptionDto(new NullPointerException("Texto inválido").toString(),
                            "Insira um texto válido."))
                    .build();
        } else if (type.isPresent() && Arrays.stream(REPORT_TYPE.values()).noneMatch(ft -> ft == type.get())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ExceptionDto(new IllegalArgumentException("Tipo de relatório inválido").toString(),
                            "Verifique se este tipo de relatório está digitado corretamente ou existe."))
                    .build();
        } else if ((generationStartYear.isPresent() && generationEndYear.isPresent()) && generationStartYear.get() > generationEndYear.get()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ExceptionDto(new IllegalArgumentException("Data inválida").toString(),
                            "Ano inicial de geração é maior que ano final."))
                    .build();
        } else if ((periodStartYear.isPresent() && periodEndYear.isPresent()) && periodStartYear.get() > periodEndYear.get()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ExceptionDto(new IllegalArgumentException("Data inválida").toString(),
                            "Ano inicial do periodo é maior que ano final."))
                    .build();
        } else if (numFailures.isPresent() && numFailures.get() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ExceptionDto(new IllegalArgumentException("Número de falhas inválido").toString(),
                            "Insira um número maior que '0'"))
                    .build();
        }

        page = page <= 0 ? 1 : page;
        try {
            var filters = new HashMap<String, Object>();
            text.ifPresent(t -> filters.put("termo", t));
            type.ifPresent(rt -> filters.put("falhaTipo", rt));
            generationStartYear.ifPresent(gsy -> filters.put("geracaoAnoInicial", gsy));
            generationEndYear.ifPresent(gey -> filters.put("geracaoAnoFinal", gey));
            periodStartYear.ifPresent(psy -> filters.put("anoInicial", psy));
            periodEndYear.ifPresent(pey -> filters.put("anoFinal", pey));
            numFailures.ifPresent(nf -> filters.put("maximoFalhasNoRelatorio", nf));

            var reports = REPO.get().stream()
                    .filter(r ->
                            (text.isEmpty() || (r.getTitle().contains(text.get()) || r.getInfo().contains(text.get()))) &&
                                    (type.isEmpty() || r.getReportType().equals(type.get())) &&
                                    (generationStartYear.isEmpty() || r.getGenerationDate().getYear() >= generationStartYear.get()) &&
                                    (generationEndYear.isEmpty() || r.getGenerationDate().getYear() <= generationEndYear.get()) &&
                                    (periodStartYear.isEmpty() || r.getPeriodInicialDate().getYear() >= periodStartYear.get()) &&
                                    (periodEndYear.isEmpty() || r.getPeriodFinalDate().getYear() <= periodEndYear.get()) &&
                                    (numFailures.isEmpty() || r.getTotalNumberOfFailures() <= numFailures.get())
                    ).sorted(ascending ?
                            orderBy.equals("date") ?
                                    Comparator.comparing(Report::getGenerationDate) :
                                    Comparator.comparing(Report::getTitle).thenComparing(Report::getInfo)
                            :
                            orderBy.equals("date") ?
                                    Comparator.comparing(Report::getGenerationDate).reversed() :
                                    Comparator.comparing(Report::getTitle).thenComparing(Report::getInfo).reversed()
                    ).toList();

            var start = (page - 1) * pageSize;
            var end = Math.min(reports.size(), start + pageSize);

            var failuresPaginated = reports.subList(start, end);

            return Response.ok(
                    new SearchDto<>(
                            new PageDto<>(page, pageSize, reports.size(), failuresPaginated),
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
    public Response getReportById(@PathParam("id") UUID id) {
        try {

            var reportOptional = REPO.getById(id);
            if (reportOptional.isPresent()) {

                return Response.ok(
                        reportOptional.get()
                ).build();

            } else {

                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(new ExceptionDto(new NotFoundException("Relatório não encontrado").toString(),
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
    public Response updateById(@PathParam("id") UUID id, Report newReport) {

        try {

            var reportOptional = REPO.getById(id);
            Report report;
            if (reportOptional.isPresent()) {
                report = reportOptional.get();
            } else {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(new ExceptionDto(new NotFoundException("Relatório não encontrado").toString(),
                                "Verifique se o ID é válido."))
                        .build();
            }

            report.updateAttributes(newReport);
            REPO.updateById(id, report);
            return Response.ok(
                    report
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
    public Response addReport(Report report) {

        if (!ReportService.createReportCheck(report)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ExceptionDto(new IllegalArgumentException("Relatório inválido").toString(),
                            "Verifique se os campos necessário estão preenchido corretamente"))
                    .build();
        }

        try {
            REPO.add(report);
            return Response.ok(
                    report
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
