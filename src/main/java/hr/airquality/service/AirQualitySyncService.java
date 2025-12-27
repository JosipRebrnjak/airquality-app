package hr.airquality.service;

import hr.airquality.model.Mreza;
import hr.airquality.model.Postaja;
import hr.airquality.repository.MrezaRepository;
import hr.airquality.repository.PostajaRepository;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;


@Stateless
public class AirQualitySyncService {

    private static final String API_URL = "http://www.ekonerg.hr/iskzl/rs/mreza/list";

    private static final Logger logger = LoggerFactory.getLogger(AirQualitySyncService.class);

    @Inject
    private MrezaRepository mrezaRepository;

    @Inject
    private PostajaRepository postajaRepository;

    @Transactional
    public void syncMreze() {
        logger.info("Početak sinkronizacije mreža s vanjskim API-jem");
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            JsonReader reader = Json.createReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            JsonArray mrezeArray = reader.readArray();

            for (var val : mrezeArray) {
                JsonObject mrezaJson = val.asJsonObject();
                String naziv = mrezaJson.getString("naziv");

                if (!naziv.equals("Mjerna mreža grada Zagreba") &&
                    !naziv.equals("Mjerna mreža Primorsko-goranske županije")) {
                    continue;
                }

                Mreza mreza = mrezaRepository.findByNaziv(naziv)
                        .orElseGet(() -> {
                            Mreza nova = new Mreza();
                            nova.setNaziv(naziv);
                            nova.setNazivEng(mrezaJson.getString("nazivEng", ""));
                            nova.setZadnjaPromjena(Timestamp.from(Instant.now()));
                            nova.setPostaje(new ArrayList<>());
                            return mrezaRepository.save(nova);
                        });

                // update postojeće mreže
                mreza.setNazivEng(mrezaJson.getString("nazivEng", ""));
                mreza.setZadnjaPromjena(Timestamp.from(Instant.now()));

                JsonArray postajeArray = mrezaJson.getJsonArray("postaje");
                if (postajeArray != null) {
                    for (var pval : postajeArray) {
                        JsonObject pJson = pval.asJsonObject();
                        String nazivPostaje = pJson.getString("naziv");

                        Postaja postaja = postajaRepository
                                .findByNazivAndMreza(nazivPostaje, mreza)
                                .orElseGet(() -> {
                                    Postaja nova = new Postaja();
                                    nova.setNaziv(nazivPostaje);
                                    nova.setMreza(mreza);
                                    return postajaRepository.save(nova);
                                });

                        postaja.setNazivEng(pJson.getString("nazivEng", ""));
                        postaja.setAktivna(pJson.getBoolean("aktivan", false));
                        postaja.setZadnjaPromjena(Timestamp.from(Instant.now()));

                        if (!mreza.getPostaje().contains(postaja)) {
                            mreza.getPostaje().add(postaja);
                        }

                        postajaRepository.update(postaja);
                    }
                }
            }

            reader.close();
            conn.disconnect();
            logger.info("Sinkronizacija mreža završena. Broj mreža: {}", mrezeArray.size());
        } catch (Exception e) {
            logger.error("Greška prilikom sync-a mreža", e);
            throw new RuntimeException("Greška prilikom sync-a mreža", e);
        }
    }
}
