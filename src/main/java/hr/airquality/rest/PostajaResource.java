package hr.airquality.rest;

import hr.airquality.dto.PostajaDTO;
import hr.airquality.service.AirQualityService;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/stations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Mjerne postaje", description = "API za postaje mjernih mreža")
public class PostajaResource {

    @EJB
    private AirQualityService airService;


    @GET
    @Path("/{mrezaNaziv}")
    @Operation(summary = "Dohvat svih postaja u traženoj mreži")
    public Response getPostajeByMreza(@PathParam("mrezaNaziv") String mrezaNaziv) {
        List<PostajaDTO> postaje = airService.getMrezaDTOByNaziv(mrezaNaziv).getPostaje();
        if (postaje == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Mreža ili postaje nisu pronađene\"}")
                    .build();
        }
        return Response.ok(postaje).build();
    }

  
    public static class PostajaUpdateDTO {
        public String nazivEng;
        public boolean aktivna;
    }

    @GET
    @Path("/{mrezaNaziv}/{postajaNaziv}")
    @Operation(summary = "Dohvat pojedine postaje u mreži")
    public PostajaDTO getPostaja(
            @PathParam("mrezaNaziv") String mrezaNaziv,
            @PathParam("postajaNaziv") String postajaNaziv) {

        PostajaDTO postajaDto = airService.getPostajaDTOByNazivAndMreza(postajaNaziv, mrezaNaziv);
        if (postajaDto == null) {
            throw new WebApplicationException("Postaja nije pronađena", 404);
        }
        return postajaDto;
    }

    @PUT
    @Path("/{postajaNaziv}")
    @Operation(summary = "Uređivanje postaje")
    public Response updatePostaja(
            @PathParam("postajaNaziv") String postajaNaziv,
            @QueryParam("mrezaNaziv") String mrezaNaziv,
            PostajaUpdateDTO update) {

        boolean updated = airService.updatePostaja(postajaNaziv, update.nazivEng, update.aktivna);
        if (!updated) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Postaja nije pronađena za update\"}")
                    .build();
        }
        return Response.ok("{\"status\":\"success\"}").build();
    }

    
    @GET
    @Path("/sync")
    @Operation(summary = "Sinkronizacija podataka sa vanjskim servisom")
    public Response syncPostaje() {
        airService.syncMreze();
        return Response.ok("{\"status\":\"success\",\"message\":\"Sync završen\"}").build();
    }
}
