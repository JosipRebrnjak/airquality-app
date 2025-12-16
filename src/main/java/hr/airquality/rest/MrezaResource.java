package hr.airquality.rest;

import hr.airquality.dto.MrezaSimpleDTO;
import hr.airquality.service.AirQualityService;

import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/*
 * REST za vanjske klijente - mreže
*/

@Path("/networks")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Mjerne mreže", description = "API za mjerne mreže")
public class MrezaResource {

    @EJB
    private AirQualityService airQualityService;

    @GET
    @Operation(summary = "Dohvat mreža")
    public List<MrezaSimpleDTO> getAllMreze() {
        return airQualityService.getAllMrezeSimple();
    }

    @GET
    @Path("/sync")
    @Operation(summary = "Sinkronizacija podataka sa vanjskim servisom")
    public Response syncPostaje() {
        airQualityService.syncMreze();
        return Response.ok("{\"status\":\"success\",\"message\":\"Sync završen\"}").build();
    }
}
