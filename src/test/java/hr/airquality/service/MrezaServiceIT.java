package hr.airquality.service;

import hr.airquality.dto.MrezaDTO;
import hr.airquality.sync.AirQualitySyncService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MrezaServiceIT {

    @Inject
    private MrezaService mrezaService;

    @Inject
    private AirQualitySyncService syncService;

    @Test
    @Transactional
    void shouldReturnMrezaWithPostajeAfterSync() {
        // Arrange
        syncService.sync();

        // Act
        MrezaDTO mreza = mrezaService.getMrezaByNaziv("Mjerna mreža grada Zagreba");

        // Assert
        assertNotNull(mreza, "Mreza ne smije biti null");
        assertEquals("Mjerna mreža grada Zagreba", mreza.getNaziv());
        assertNotNull(mreza.getPostaje(), "Postaje ne smiju biti null");
        assertFalse(mreza.getPostaje().isEmpty(), "Mreza mora imati barem jednu postaju");
    }

    @Test
    @Transactional
    void shouldReturnNullForUnknownMreza() {
        MrezaDTO mreza = mrezaService.getMrezaByNaziv("Nepostojeća mreža");
        assertNull(mreza);
    }
}
