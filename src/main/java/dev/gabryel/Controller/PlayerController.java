package dev.gabryel.Controller;

import dev.gabryel.DTO.PlayerDTO;
import dev.gabryel.DTO.UserDTO;
import dev.gabryel.Entity.Player;
import dev.gabryel.Entity.PokerTable;
import dev.gabryel.Entity.User;
import dev.gabryel.Service.PlayerService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Players", description = "Gerenciar jogadores nas mesas de poker")
@SecuritySchemes(value = {
        @SecurityScheme(securitySchemeName = "apiKey",
                type = SecuritySchemeType.HTTP,
                scheme = "Bearer")}
)
@Path("/poker-tables/{tableId}")
@RolesAllowed("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlayerController {

    @Inject
    PlayerService playerService;

    @POST
    @Transactional
    @Path("/players")
    public Response addUserToTable(@PathParam("tableId") Long tableId, PlayerDTO player) {
        try {
            PokerTable table = PokerTable.findById(tableId);
            User user = User.findById(player.userId);

            playerService.validatePlayer(tableId, user, table, player.userId);

            Player newPlayer = new Player();
            newPlayer.pokerTable = table;
            newPlayer.user = user;

            newPlayer.persist();

           return Response.ok().entity("{\"message\": \"Usuário adicionado à mesa\"}").build();
        } catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Transactional
    @Path("/winner")
    public Response defineWinner(@PathParam("tableId") Long tableId, PlayerDTO player) {
        try {
            PokerTable table = PokerTable.findById(tableId);
            User user = User.findById(player.userId);

            playerService.validateWinner(table);

            UserDTO userDTO = new UserDTO();

            userDTO.username = user.username;
            userDTO.userId = user.id;

            return Response.status(Response.Status.ACCEPTED).entity(userDTO).build();
        } catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
