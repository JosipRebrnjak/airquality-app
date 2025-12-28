package hr.airquality.service;

import hr.airquality.dto.PostajaDTO;
import hr.airquality.exception.NotFoundException;
import hr.airquality.sync.AirQualitySyncService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostajaServiceIT {

    @Inject
    private PostajaService postajaService;

    @Inject
    private AirQualitySyncService syncService;

    @Test
    @Transactional
    void shouldGetAndUpdatePostaja() {
        // Arrange – napuni bazu
        syncService.sync();

        // Act – dohvat postaje
        PostajaDTO postaja = postajaService.getPostajaByNaziv("Ksaverska cesta");

        // Assert – dohvat
        assertNotNull(postaja);
        assertEquals("Ksaverska cesta", postaja.getNaziv());

        // Act – update
        postaja.setNazivEng("Ksaver street");
        postaja.setAktivna(false);

        postajaService.updatePostaja(postaja.getNaziv(), postaja);

        // Assert – ponovno dohvatimo iz baze
        PostajaDTO updated = postajaService.getPostajaByNaziv("Ksaverska cesta");
        assertEquals("Ksaverska cesta", updated.getNazivEng());
        assertFalse(updated.isAktivna());
    }

    @Test
    @Transactional
    void shouldThrowNotFoundExceptionForUnknownPostaja() {
        PostajaDTO dto = new PostajaDTO("NePostoji", "X", true, "MrezaX");

        assertThrows(
                NotFoundException.class,
                () -> postajaService.updatePostaja("NePostoji", dto)
        );
    }
}
