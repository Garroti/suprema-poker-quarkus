package dev.gabryel.Controller;

import dev.gabryel.DTO.AuthDTO;
import dev.gabryel.DTO.TokenDTO;
import dev.gabryel.Entity.User;
import dev.gabryel.Service.UserService;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.Instant;

@Tag(name = "Auth", description = "Login jogador")
@Path("/auth")
public class AuthController {

    @Inject
    UserService userService;

    @POST
    @Path("/login")
    public Response login(AuthDTO auth) {

        User user = userService.getUserByUsername(auth.username);

        if (!user.username.equals(auth.username) || !user.password.equals(auth.password)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Usuário ou senha inválidos\"}")
                    .build();
        }

        String token = Jwt.issuer("suprema")
                .upn(auth.password)
                .groups("user")
                .expiresAt(Instant.now().plusSeconds(3600).getEpochSecond())
                .sign();

        return Response.ok(new TokenDTO(token)).build();
    }
}
