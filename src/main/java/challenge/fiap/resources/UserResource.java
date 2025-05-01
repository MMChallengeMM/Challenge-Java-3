package challenge.fiap.resources;

import challenge.fiap.dtos.ExceptionDto;
import challenge.fiap.dtos.PageDto;
import challenge.fiap.dtos.UserDto;
import challenge.fiap.models.Admin;
import challenge.fiap.models.Operator;
import challenge.fiap.models.User;
import challenge.fiap.repositories.UserRepo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Comparator;
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
            int pageSize
    ) {
        if ((username.isPresent() && username.get().isEmpty())) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
}