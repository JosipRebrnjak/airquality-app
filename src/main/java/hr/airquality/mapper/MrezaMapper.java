package hr.airquality.mapper;

import hr.airquality.dto.MrezaDTO;
import hr.airquality.dto.MrezaSimpleDTO;
import hr.airquality.dto.PostajaDTO;
import hr.airquality.model.Mreza;

import jakarta.ejb.Stateless;
import java.util.stream.Collectors;

@Stateless
public class MrezaMapper {

    public MrezaDTO toDto(Mreza mreza) {
        if (mreza == null) return null;

        return new MrezaDTO(
                mreza.getNaziv(),
                mreza.getNazivEng(),
                mreza.getPostaje()
                        .stream()
                        .map(p -> new PostajaDTO(
                                p.getNaziv(),
                                p.getNazivEng(),
                                p.getAktivna(),
                                mreza.getNaziv()))
                        .collect(Collectors.toList())
        );
    }

    public MrezaSimpleDTO toSimpleDto(Mreza mreza) {
        if (mreza == null) return null;

        return new MrezaSimpleDTO(
                mreza.getNaziv(),
                mreza.getNazivEng()
        );
    }
}
