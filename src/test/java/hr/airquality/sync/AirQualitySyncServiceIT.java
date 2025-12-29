package hr.airquality.sync;

import hr.airquality.dto.MrezaDTO;
import hr.airquality.service.MrezaService;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AirQualitySyncServiceIT {

    @PersistenceContext(unitName = "AirPU")
    private EntityManager em;

    @Inject
    private AirQualitySyncService syncService;

    @Inject
    private MrezaService mrezaService;


    @Test
    @Transactional
    void shouldSyncMrezaAndPostaje() {
        // Pokreni sync koji popunjava H2 DB
        syncService.sync();

        // Provjera mreže
        MrezaDTO mreza = mrezaService.getMrezaByNaziv("Mjerna mreža grada Zagreba");
        assertNotNull(mreza, "Mreza ne smije biti null nakon sync-a");
        assertFalse(mreza.getPostaje().isEmpty(), "Mreza mora imati barem jednu postaju");

        // Provjeri specifičnu postaju
        assertTrue(
                mreza.getPostaje().stream()
                        .anyMatch(p -> "Ksaverska cesta".equals(p.getNaziv())),
                "Postaja 'Ksaverska cesta' mora postojati u mreži"
        );
    }
}
