package hr.airquality.service;

import hr.airquality.dto.PostajaDTO;
import hr.airquality.exception.NotFoundException;
import hr.airquality.mapper.PostajaMapper;
import hr.airquality.model.Mreza;
import hr.airquality.model.Postaja;
import hr.airquality.repository.MrezaRepository;
import hr.airquality.repository.PostajaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class PostajaService {

    private static final Logger log = LoggerFactory.getLogger(PostajaService.class);

    @Inject
    private PostajaRepository postajaRepository;

    @Inject
    private MrezaRepository mrezaRepository;

    @Inject
    private PostajaMapper mapper;

    public PostajaService(){

    }

    // =========================
    // READ
    // =========================

    public List<PostajaDTO> getAllPostaje() {
        log.info("Dohvat svih postaja");
        return postajaRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public PostajaDTO getPostajaByNaziv(String naziv) {
        log.info("Dohvat postaje po nazivu: {}", naziv);

        Postaja postaja = postajaRepository.findByNaziv(naziv)
                .orElseThrow(() -> {
                    log.warn("Postaja '{}' nije pronađena", naziv);
                    return new NotFoundException("Postaja '" + naziv + "' nije pronađena");
                });

        return mapper.toDto(postaja);
    }

    public PostajaDTO getPostajaByNazivAndMreza(String naziv, String mrezaNaziv) {
        log.info("Dohvat postaje '{}' u mreži '{}'", naziv, mrezaNaziv);

        Mreza mreza = mrezaRepository.findByNaziv(mrezaNaziv)
                .orElseThrow(() -> {
                    log.warn("Mreža '{}' nije pronađena", mrezaNaziv);
                    return new NotFoundException("Mreža '" + mrezaNaziv + "' nije pronađena");
                });

        Postaja postaja = postajaRepository.findByNazivAndMreza(naziv, mreza)
                .orElseThrow(() -> {
                    log.warn("Postaja '{}' nije pronađena u mreži '{}'", naziv, mrezaNaziv);
                    return new NotFoundException(
                            "Postaja '" + naziv + "' nije pronađena u mreži '" + mrezaNaziv + "'");
                });

        return mapper.toDto(postaja);
    }

    // =========================
    // UPDATE
    // =========================

    public void updatePostaja(String naziv, PostajaDTO dto) {
        log.info("Ažuriranje postaje '{}'", naziv);

        Postaja postaja = postajaRepository.findByNaziv(naziv)
                .orElseThrow(() -> {
                    log.warn("Postaja '{}' za update nije pronađena", naziv);
                    return new NotFoundException("Postaja '" + naziv + "' nije pronađena");
                });

        mapper.updateFromDto(postaja, dto);
        postajaRepository.update(postaja);

        log.info("Postaja '{}' uspješno ažurirana", naziv);
    }
}
