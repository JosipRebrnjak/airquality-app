package hr.airquality.rest;


import java.util.HashMap;
import java.util.Map;

import hr.airquality.service.MrezaService;
import hr.airquality.sync.AirQualitySyncService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/networks")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class MrezaResource {

    @Inject
    private MrezaService mrezaService;

    @Inject
    private AirQualitySyncService syncService;

    @GET
    public Response getAllMreze() {
        return Response.ok(mrezaService.getAllMreze()).build();
    }

    @GET
    @Path("/simple")
    public Response getAllMrezeSimple() {
        return Response.ok(mrezaService.getAllMrezeSimple()).build();
    }

    @GET
    @Path("/sync")
    public Response syncNetworks() {
        Map<String, Object> result = new HashMap<>();
        try {
            syncService.sync();
            result.put("status", "success");
            result.put("message", "Sinkronizacija završena");
            return Response.ok(result).build();
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "Greška prilikom sinkronizacije: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result).build();
        }
    }

    @GET
    @Path("/{naziv}")
    public Response getMreza(@PathParam("naziv") String naziv) {
        return Response.ok(mrezaService.getMrezaByNaziv(naziv)).build();
    }
}
