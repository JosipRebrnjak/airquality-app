package hr.airquality.rest;

import hr.airquality.dto.PostajaDTO;
import hr.airquality.service.PostajaService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/stations")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostajaResource {

    @Inject
    private PostajaService postajaService;

    @GET
    public Response getAllPostaje() {
        List<PostajaDTO> postaje = postajaService.getAllPostaje();
        return Response.ok(postaje).build();
    }

    @GET
    @Path("/{naziv}")
    public Response getPostaja(@PathParam("naziv") String naziv) {
        PostajaDTO postaja = postajaService.getPostajaByNaziv(naziv);
        return Response.ok(postaja).build();
    }

    @GET
    @Path("/by-network/{mrezaNaziv}")
    public Response getPostajeByMreza(@PathParam("mrezaNaziv") String mrezaNaziv) {
        List<PostajaDTO> postaje = postajaService.getPostajeByMreza(mrezaNaziv);
        return Response.ok(postaje).build();
    }

    @PUT
    @Path("/{naziv}")
    public Response updatePostaja(@PathParam("naziv") String naziv, PostajaDTO dto) {
        postajaService.updatePostaja(naziv, dto);
        return Response.noContent().build();
    }

}
