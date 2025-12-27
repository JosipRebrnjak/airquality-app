package hr.airquality.rest;

import hr.airquality.dto.PostajaDTO;
import hr.airquality.service.PostajaService;
import hr.airquality.exception.NotFoundException;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/stations")
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
    @Path("/{naziv}/{mreza}")
    public Response getPostajaByMreza(
            @PathParam("naziv") String naziv,
            @PathParam("mreza") String mrezaNaziv) {

        PostajaDTO postaja = postajaService.getPostajaByNazivAndMreza(naziv, mrezaNaziv);
        return Response.ok(postaja).build();
    }

    @PUT
    @Path("/{naziv}")
    public Response updatePostaja(
            @PathParam("naziv") String naziv,
            PostajaDTO postajaDto) {

        boolean success = postajaService.updatePostaja(
                naziv,
                postajaDto.getNazivEng(),
                postajaDto.isAktivna()
        );

        if (!success) {
            throw new NotFoundException("Postaja '" + naziv + "' nije pronađena");
        }

        return Response.ok().build();
    }
}
