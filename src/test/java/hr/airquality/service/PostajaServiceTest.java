package hr.airquality.service;

import hr.airquality.dto.PostajaDTO;
import hr.airquality.exception.NotFoundException;
import hr.airquality.mapper.PostajaMapper;
import hr.airquality.model.Mreza;
import hr.airquality.model.Postaja;
import hr.airquality.repository.MrezaRepository;
import hr.airquality.repository.PostajaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostajaServiceTest {

    @Mock
    PostajaRepository postajaRepository;

    @Mock
    MrezaRepository mrezaRepository;

    @Mock
    PostajaMapper mapper;

    @InjectMocks
    PostajaService postajaService;

    @Test
    void shouldReturnPostajaByNaziv() {
        // arrange
        Mreza mreza = new Mreza("Mreza1");
        Postaja postaja = new Postaja("Postaja1", mreza);
        PostajaDTO dto = new PostajaDTO("Postaja1", "Station1", true, "Mreza1");

        when(postajaRepository.findByNaziv("Postaja1"))
                .thenReturn(Optional.of(postaja));
        when(mapper.toDto(postaja))
                .thenReturn(dto);

        // act
        PostajaDTO result = postajaService.getPostajaByNaziv("Postaja1");

        // assert
        assertNotNull(result);
        assertEquals("Postaja1", result.getNaziv());
        verify(postajaRepository).findByNaziv("Postaja1");
        verify(mapper).toDto(postaja);
    }

    @Test
    void shouldThrowExceptionWhenPostajaNotFound() {
        when(postajaRepository.findByNaziv("Nepoznata"))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> postajaService.getPostajaByNaziv("Nepoznata")
        );
    }

    @Test
    void shouldUpdatePostaja() {
        Mreza mreza = new Mreza("Mreza1");
        Postaja postaja = new Postaja("Postaja1", mreza);
        PostajaDTO dto = new PostajaDTO("Postaja1", "NewName", false, "Mreza1");

        when(postajaRepository.findByNaziv("Postaja1"))
                .thenReturn(Optional.of(postaja));

        // act
        postajaService.updatePostaja("Postaja1", dto);

        // assert
        verify(mapper).updateFromDto(postaja, dto);
        verify(postajaRepository).update(postaja);
    }

    @Test
    void shouldReturnAllPostaje() {
        Postaja postaja = new Postaja("P1", new Mreza("M1"));
        PostajaDTO dto = new PostajaDTO("P1", "E1", true, "M1");

        when(postajaRepository.findAll()).thenReturn(List.of(postaja));
        when(mapper.toDto(postaja)).thenReturn(dto);

        List<PostajaDTO> result = postajaService.getAllPostaje();

        assertEquals(1, result.size());
        verify(postajaRepository).findAll();
    }
}
