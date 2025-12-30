package hr.airquality.sync;

import hr.airquality.model.Mreza;
import hr.airquality.model.Postaja;
import hr.airquality.repository.MrezaRepository;
import hr.airquality.repository.PostajaRepository;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.json.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;

@ApplicationScoped
public class AirQualitySyncService {

    private static final Logger log =
            LoggerFactory.getLogger(AirQualitySyncService.class);

    private final SyncClient client;
    private final SyncMapper mapper;
    private final MrezaRepository mrezaRepository;
    private final PostajaRepository postajaRepository;

    @Inject
    public AirQualitySyncService(SyncClient client,
                                 SyncMapper mapper,
                                 MrezaRepository mrezaRepository,
                                 PostajaRepository postajaRepository) {
        this.client = client;
        this.mapper = mapper;
        this.mrezaRepository = mrezaRepository;
        this.postajaRepository = postajaRepository;
    }

    @Transactional
    public void sync() {
        log.info("Pokrećem sinkronizaciju Ekonerg mreža");

        JsonArray mreze = client.fetchMreze();

        for (JsonValue value : mreze) {
            JsonObject jsonMreza = value.asJsonObject();
            String naziv = jsonMreza.getString("naziv");

            if (!isSupported(naziv)) continue;

            // =========================
            // Mreza
            // =========================
            Mreza mreza = mrezaRepository.findByNaziv(naziv)
                    .orElseGet(() -> {
                        log.info("Kreiram novu mrežu: {}", naziv);
                        Mreza m = new Mreza(naziv);
                        mrezaRepository.save(m);
                        return m;
                    });

            mapper.updateMreza(mreza, jsonMreza);
            mreza.setZadnjaPromjena(Timestamp.from(Instant.now()));

            // =========================
            // Postaje
            // =========================
            JsonArray postaje = jsonMreza.getJsonArray("postaje");
            if (postaje == null) continue;

            for (JsonValue pv : postaje) {
                JsonObject jsonPostaja = pv.asJsonObject();
                String nazivPostaje = jsonPostaja.getString("naziv");

                Postaja postaja = postajaRepository
                        .findByNazivAndMreza(nazivPostaje, mreza)
                        .orElseGet(() -> {
                            log.info("Dodajem postaju '{}' u mrežu '{}'", nazivPostaje, naziv);
                            Postaja p = mapper.toPostaja(jsonPostaja, mreza);
                            postajaRepository.save(p);
                            mreza.getPostaje().add(p);
                            return p;
                        });

                mapper.updatePostaja(postaja, jsonPostaja);
                postaja.setZadnjaPromjena(Timestamp.from(Instant.now()));

                if (!mreza.getPostaje().contains(postaja)) {
                    mreza.getPostaje().add(postaja);
                }
            }
        }

        log.info("Sinkronizacija završena");
    }

    private boolean isSupported(String naziv) {
        return naziv.equals("Mjerna mreža grada Zagreba")
                || naziv.equals("Mjerna mreža Primorsko-goranske županije");
    }
}
