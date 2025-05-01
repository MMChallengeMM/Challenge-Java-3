package challenge.fiap.resources;

import challenge.fiap.dtos.ExceptionDto;
import challenge.fiap.dtos.PageDto;
import challenge.fiap.dtos.SearchDto;
import challenge.fiap.dtos.UserDto;
import challenge.fiap.models.Admin;
import challenge.fiap.models.Operator;
import challenge.fiap.models.User;
import challenge.fiap.repositories.UserRepo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

@Path("/users")
public class UserResource {

    private final UserRepo REPO = new UserRepo();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(
            @QueryParam("page") @DefaultValue("1")
            int page,

            @QueryParam("size") @DefaultValue("20")
            int pageSize
    ) {

        page = page <= 0 ? 1 : page;
        try {
            var users = REPO.get().stream()
                    .sorted(Comparator.comparing(User::getUsername))
                    .toList();

            var start = (page - 1) * pageSize;
            var end = Math.min(users.size(), start + pageSize);

            var usersPaginated = users.subList(start, end).stream()
                    .map(u -> new UserDto(
                                    u.getId(),
                                    u.getUsername(),
                                    u.getEmail(),
                                    u.isDeleted(),
                                    u instanceof Admin ? ((Admin) u).getAcessLevel() : -1,
                                    u instanceof Operator ? ((Operator) u).getSector() : null
                            )
                    ).toList();

            return Response.ok(
                    new PageDto<>(
                            page,
                            pageSize,
                            users.size(),
                            usersPaginated)
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
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersFiltered(

            @QueryParam("name")
            Optional<String> username,

            @QueryParam("email")
            Optional<String> email,

            @QueryParam("access")
            Optional<Integer> accessLevel,

            @QueryParam("sector")
            Optional<String> sector,

            @QueryParam("page") @DefaultValue("1")
            int page,

            @QueryParam("size") @DefaultValue("20")
            int pageSize,

            @QueryParam("orderby") @DefaultValue("name")
            String orderBy,

            @QueryParam("ascending") @DefaultValue("false")
            boolean ascending
    ) {
        if ((username.isPresent() && username.get().isEmpty()) ||
                (email.isPresent() && email.get().isEmpty()) ||
                (sector.isPresent() && sector.get().isEmpty())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ExceptionDto(new IllegalArgumentException("Texto inválido").toString(),
                            "Por favor insira algum termo nos campos presentes"))
                    .build();
        } else if (accessLevel.isPresent() && accessLevel.get() < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ExceptionDto(new IllegalArgumentException("Nivel de acesso inválido").toString(),
                            "Verifique se o acesso é '>= 0'"))
                    .build();
        }

        page = page <= 0 ? 1 : page;
        try {
            var filters = new HashMap<String, Object>();
            username.ifPresent(un -> filters.put("username", un));
            email.ifPresent(e -> filters.put("email", e));
            sector.ifPresent(s -> filters.put("setor", s));
            accessLevel.ifPresent(al -> filters.put("nivelAcesso", al));

            var users = REPO.get().stream()
                    .filter(u ->
                            (username.isEmpty() || u.getUsername().contains(username.get())) &&
                                    (email.isEmpty() || u.getEmail().contains(email.get())) &&
                                    (sector.isEmpty() || (u instanceof Operator &&
                                            ((Operator) u).getSector().contains(sector.get()))) &&
                                    (accessLevel.isEmpty() || (u instanceof Admin &&
                                            ((Admin) u).getAcessLevel() <= accessLevel.get()))
                    ).sorted(ascending ?
                            orderBy.equals("name") ?
                                    Comparator.comparing(User::getUsername) :
                                    Comparator.comparing(User::getEmail) :
                            orderBy.equals("name") ?
                                    Comparator.comparing(User::getUsername).reversed() :
                                    Comparator.comparing(User::getEmail).reversed()
                    )
                    .toList();

            var start = (page - 1) * pageSize;
            var end = Math.min(users.size(), start + pageSize);

            var usersPaginated = users.subList(start, end).stream()
                    .map(u -> new UserDto(
                                    u.getId(),
                                    u.getUsername(),
                                    u.getEmail(),
                                    u.isDeleted(),
                                    u instanceof Admin ? ((Admin) u).getAcessLevel() : -1,
                                    u instanceof Operator ? ((Operator) u).getSector() : null
                            )
                    ).toList();

            return Response.ok(
                    new SearchDto<>(
                            new PageDto<>(page, pageSize, users.size(), usersPaginated),
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
}