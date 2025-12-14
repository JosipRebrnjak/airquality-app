package hr.airquality.rest;

import hr.airquality.dto.MrezaSimpleDTO;
import hr.airquality.service.AirQualityService;

import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

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
}
