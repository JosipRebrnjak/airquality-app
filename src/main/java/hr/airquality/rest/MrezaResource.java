package hr.airquality.rest;


import hr.airquality.service.MrezaService;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/networks")
@Produces(MediaType.APPLICATION_JSON)
public class MrezaResource {

    @Inject
    private MrezaService mrezaService;

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
    @Path("/{naziv}")
    public Response getMreza(@PathParam("naziv") String naziv) {
        return Response.ok(mrezaService.getMrezaByNaziv(naziv)).build();
    }
}
