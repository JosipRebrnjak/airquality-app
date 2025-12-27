package hr.airquality.service;

import hr.airquality.dto.PostajaDTO;
import hr.airquality.model.Postaja;
import hr.airquality.model.Mreza;
import hr.airquality.repository.PostajaRepository;
import hr.airquality.repository.MrezaRepository;
import hr.airquality.exception.NotFoundException;
import hr.airquality.mapper.PostajaMapper;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class PostajaService {

    private static final Logger logger = LoggerFactory.getLogger(PostajaService.class);

    @Inject
    private PostajaRepository postajaRepository;

    @Inject
    private MrezaRepository mrezaRepository;

    @Inject
    private PostajaMapper mapper;

    public PostajaDTO getPostajaByNaziv(String naziv) {
        logger.info("Dohvat postaje s nazivom '{}'", naziv);
        Postaja postaja = postajaRepository.findByNaziv(naziv)
                .orElseThrow(() -> {
                    logger.warn("Postaja '{}' nije pronađena", naziv);
                    return new NotFoundException("Postaja '" + naziv + "' nije pronađena");
                });

        return mapToDto(postaja);
    }

    public PostajaDTO getPostajaByNazivAndMreza(String naziv, String mrezaNaziv) {
        Mreza mreza = mrezaRepository.findByNaziv(mrezaNaziv)
                .orElseThrow(() -> new NotFoundException("Mreža '" + mrezaNaziv + "' nije pronađena"));

        Postaja postaja = postajaRepository.findByNazivAndMreza(naziv, mreza)
                .orElseThrow(() -> new NotFoundException(
                        "Postaja '" + naziv + "' nije pronađena u mreži '" + mrezaNaziv + "'"));

        return mapToDto(postaja);
    }

    public boolean updatePostaja(String naziv, String nazivEng, boolean aktivna) {
        Postaja postaja = postajaRepository.findByNaziv(naziv)
                .orElseThrow(() -> new NotFoundException("Postaja '" + naziv + "' nije pronađena"));

        postaja.setNazivEng(nazivEng);
        postaja.setAktivna(aktivna);

        postajaRepository.update(postaja);
        return true;
    }

    public List<PostajaDTO> getAllPostaje() {
        return postajaRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private PostajaDTO mapToDto(Postaja postaja) {
        return mapper.toDto(postaja);
    }
}
