package hr.airquality.mapper;

import hr.airquality.dto.PostajaDTO;
import hr.airquality.model.Postaja;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostajaMapper {

    public PostajaDTO toDto(Postaja postaja) {
        if (postaja == null) return null;

        return new PostajaDTO(
                postaja.getNaziv(),
                postaja.getNazivEng(),
                postaja.getAktivna(),
                postaja.getMreza().getNaziv()
        );
    }

    public void updateFromDto(Postaja postaja, PostajaDTO dto) {
        postaja.setNazivEng(dto.getNazivEng());
        postaja.setAktivna(dto.isAktivna());
    }

    public Postaja toEntity(PostajaDTO dto, hr.airquality.model.Mreza mreza) {
        if (dto == null) return null;

        Postaja postaja = new Postaja(dto.getNaziv(), mreza);
        postaja.setNazivEng(dto.getNazivEng());
        postaja.setAktivna(dto.isAktivna());

        return postaja;
    }
}
