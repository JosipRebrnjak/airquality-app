package hr.airquality.service;

import hr.airquality.dto.MrezaDTO;
import hr.airquality.exception.NotFoundException;
import hr.airquality.mapper.MrezaMapper;
import hr.airquality.model.Mreza;
import hr.airquality.repository.MrezaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MrezaServiceTest {

    @InjectMocks
    private MrezaService service;

    @Mock
    private MrezaRepository repository;

    @Mock
    private MrezaMapper mapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetMrezaDtoByNaziv() {
        Mreza mreza = new Mreza("TestMreza");
        MrezaDTO dto = new MrezaDTO("TestMreza", "TestMrezaEng", null);

        when(repository.findByNaziv("TestMreza")).thenReturn(Optional.of(mreza));
        when(mapper.toDto(mreza)).thenReturn(dto);

        MrezaDTO result = service.getMrezaByNaziv("TestMreza");
        assertEquals("TestMreza", result.getNaziv());
    }

    @Test
    void shouldThrowNotFoundExceptionIfMrezaNotExists() {
        when(repository.findByNaziv("Unknown")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getMrezaByNaziv("Unknown"));
    }
}
