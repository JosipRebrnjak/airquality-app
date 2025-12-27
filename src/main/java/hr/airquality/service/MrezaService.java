package hr.airquality.service;

import hr.airquality.dto.MrezaDTO;
import hr.airquality.dto.MrezaSimpleDTO;
import hr.airquality.mapper.MrezaMapper;
import hr.airquality.model.Mreza;
import hr.airquality.repository.MrezaRepository;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class MrezaService {

    @Inject
    private MrezaRepository mrezaRepository;

    @Inject
    private MrezaMapper mapper;

    public List<MrezaSimpleDTO> getAllMrezeSimple() {
        return mrezaRepository.findAll()
                .stream()
                .map(mapper::toSimpleDto)
                .toList();
    }

    public MrezaDTO getMrezaByNaziv(String naziv) {
        Mreza mreza = mrezaRepository.findByNaziv(naziv)
                .orElse(null);

        return mapper.toDto(mreza);
    }
}

