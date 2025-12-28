package hr.airquality.service;

import hr.airquality.dto.MrezaDTO;
import hr.airquality.dto.MrezaSimpleDTO;
import hr.airquality.exception.NotFoundException;
import hr.airquality.mapper.MrezaMapper;
import hr.airquality.model.Mreza;
import hr.airquality.repository.MrezaRepository;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Stateless
public class MrezaService {

    private static final Logger log = LoggerFactory.getLogger(MrezaService.class);

    private final MrezaRepository mrezaRepository;
    private final MrezaMapper mapper;

    @Inject
    public MrezaService(MrezaRepository mrezaRepository,
                        MrezaMapper mapper) {
        this.mrezaRepository = mrezaRepository;
        this.mapper = mapper;
    }

    // =========================
    // READ
    // =========================

    public List<MrezaDTO> getAllMreze() {
        log.info("Dohvat svih mreža");

        return mrezaRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<MrezaSimpleDTO> getAllMrezeSimple() {
        log.info("Dohvat svih mreža (simple)");

        return mrezaRepository.findAll()
                .stream()
                .map(mapper::toSimpleDto)
                .toList();
    }

    public MrezaDTO getMrezaByNaziv(String naziv) {
        log.info("Dohvat mreže po nazivu: {}", naziv);

        Mreza mreza = mrezaRepository.findByNaziv(naziv)
                .orElseThrow(() -> {
                    log.warn("Mreža '{}' nije pronađena", naziv);
                    return new NotFoundException("Mreža '" + naziv + "' nije pronađena");
                });

        return mapper.toDto(mreza);
    }
}
