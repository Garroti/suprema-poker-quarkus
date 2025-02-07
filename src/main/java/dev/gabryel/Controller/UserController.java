package dev.gabryel.Controller;

import java.util.*;

import dev.gabryel.DTO.UserDTO;
import dev.gabryel.Entity.User;
import dev.gabryel.Service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.*;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@SecuritySchemes(value = {
        @SecurityScheme(securitySchemeName = "apiKey",
                type = SecuritySchemeType.HTTP,
                scheme = "Bearer")}
)
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Users", description = "Criar e listar jogadores")
public class UserController {

    @Inject
    UserService userService;

    @GET
    @RolesAllowed("user")
    public List<User> list() {
        return User.listAll();
    }

    @POST
    @Transactional
    public Response createUser(@Valid User user) {
        try {
            userService.validateUser(user);

            user.persist();
            UserDTO userDTO = new UserDTO(user.id);
            return Response.status(Response.Status.CREATED).entity(userDTO).build();
        } catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}
