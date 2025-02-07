package dev.gabryel.Controller;

import dev.gabryel.DTO.PokerTableDTO;
import dev.gabryel.Entity.PokerTable;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.*;

@Tag(name = "Poker Tables", description = "Criar e listar mesas de poker")
@SecuritySchemes(value = {
        @SecurityScheme(securitySchemeName = "apiKey",
                type = SecuritySchemeType.HTTP,
                scheme = "Bearer")}
)
@Path("/poker-tables")
@RolesAllowed("user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PokerTableController {

    @GET
    public List<PokerTable> list() {
        return PokerTable.listAll();
    }

    @POST
    @Transactional
    public Response createPokerTable(@Valid PokerTable poker) {
        try {
            poker.persist();
            PokerTableDTO pokerTableDTO = new PokerTableDTO(poker.id);
            return Response.status(Response.Status.CREATED).entity(pokerTableDTO).build();
        } catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}
