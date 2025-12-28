package hr.airquality.mapper;

import static org.junit.jupiter.api.Assertions.*;

import hr.airquality.dto.PostajaDTO;
import hr.airquality.model.Mreza;
import hr.airquality.model.Postaja;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit tests for PostajaMapper")
class PostajaMapperTest {

    private final PostajaMapper mapper = new PostajaMapper();

    @Test
    @DisplayName("Should correctly map Postaja entity to PostajaDTO")
    void shouldMapPostajaToDtoCorrectly() {
        Mreza mreza = new Mreza("Mreza1");
        Postaja postaja = new Postaja("Postaja1", mreza);
        postaja.setNazivEng("Station1");
        postaja.setAktivna(true);

        PostajaDTO dto = mapper.toDto(postaja);

        assertEquals("Postaja1", dto.getNaziv());
        assertEquals("Station1", dto.getNazivEng());
        assertTrue(dto.isAktivna());
    }

    @Test
    @DisplayName("Should correctly map PostajaDTO back to Postaja entity")
    void shouldMapDtoBackToPostajaCorrectly() {
        Mreza mreza = new Mreza("Mreza2");
        PostajaDTO dto = new PostajaDTO("Postaja2", "NewName", true, "Mreza2");

        Postaja postaja = new Postaja("Postaja2", mreza);
        postaja.setNazivEng("OldName");
        postaja.setAktivna(false);

        mapper.updateFromDto(postaja, dto);

        assertEquals("NewName", postaja.getNazivEng());
        assertTrue(postaja.getAktivna());
    }

    @Test
    @DisplayName("Should return null when mapping a null entity to DTO")
    void shouldReturnNullWhenEntityIsNull() {
        assertNull(mapper.toDto(null));
    }

    @Test
    @DisplayName("Should return null when mapping a null DTO to entity")
    void shouldReturnNullWhenDtoIsNull() {
        Mreza mreza = new Mreza("Mreza1");
        assertNull(mapper.toEntity(null, mreza));
    }
}
