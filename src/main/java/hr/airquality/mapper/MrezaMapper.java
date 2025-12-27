package hr.airquality.mapper;

import hr.airquality.dto.MrezaDTO;
import hr.airquality.dto.MrezaSimpleDTO;
import hr.airquality.dto.PostajaDTO;
import hr.airquality.model.Mreza;
import hr.airquality.model.Postaja;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MrezaMapper {

    // entitet → DTO
    public MrezaDTO toDto(Mreza mreza) {
        if (mreza == null) return null;

        List<PostajaDTO> postaje = mreza.getPostaje()
                .stream()
                .map(this::postajaToDto)
                .collect(Collectors.toList());

        return new MrezaDTO(
                mreza.getNaziv(),
                mreza.getNazivEng(),
                postaje
        );
    }

    public MrezaSimpleDTO toSimpleDto(Mreza mreza) {
        if (mreza == null) return null;
        return new MrezaSimpleDTO(
                mreza.getNaziv(),
                mreza.getNazivEng()
        );
    }

    // pojedinačna postaja
    public PostajaDTO postajaToDto(Postaja postaja) {
        if (postaja == null) return null;
        return new PostajaDTO(
                postaja.getNaziv(),
                postaja.getNazivEng(),
                postaja.getAktivna(),
                postaja.getMreza().getNaziv()
        );
    }

    // opcionalno: DTO → entitet (za POST/PUT)
    public Mreza toEntity(MrezaDTO dto) {
        if (dto == null) return null;

        Mreza mreza = new Mreza();
        mreza.setNaziv(dto.getNaziv());
        mreza.setNazivEng(dto.getNazivEng());
        // Postaje se mogu mapirati kasnije
        return mreza;
    }
}
