package hr.airquality.rest;

import hr.airquality.dto.MrezaDTO;
import hr.airquality.dto.MrezaSimpleDTO;
import hr.airquality.service.MrezaService;
import hr.airquality.exception.NotFoundException;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/networks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MrezaResource {

    @Inject
    private MrezaService mrezaService;

    @GET
    public Response getAllMrezeSimple() {
        List<MrezaSimpleDTO> mreze = mrezaService.getAllMrezeSimple();
        return Response.ok(mreze).build();
    }

    @GET
    @Path("/{naziv}")
    public Response getMreza(@PathParam("naziv") String naziv) {
        MrezaDTO mreza = mrezaService.getMrezaByNaziv(naziv);

        if (mreza == null) {
            throw new NotFoundException("Mreža sa nazivom '" + naziv + "' nije pronađena");
        }

        return Response.ok(mreza).build();
    }
}
