package hr.airquality.service;

import hr.airquality.model.Mreza;
import hr.airquality.model.Postaja;
import hr.airquality.dto.MrezaDTO;
import hr.airquality.dto.MrezaSimpleDTO;
import hr.airquality.dto.PostajaDTO;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.json.*;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AirQualityService {

    @PersistenceContext(unitName = "AirPU")
    private EntityManager em;

    private static final String API_URL = "http://www.ekonerg.hr/iskzl/rs/mreza/list";

    /**
     * Sinkronizacija mreža i postaja iz ekonerg.hr u bazu
     */
    @Transactional
    public void syncMreze() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            JsonReader reader = Json.createReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            JsonArray mrezeArray = reader.readArray(); 

            for (JsonValue val : mrezeArray) {
                JsonObject mrezaJson = val.asJsonObject();
                String naziv = mrezaJson.getString("naziv");

                // Dohvati filtrirane mreže
                if (!naziv.equals("Mjerna mreža grada Zagreba") &&
                        !naziv.equals("Mjerna mreža Primorsko-goranske županije")) {
                    continue;
                }

                
                Mreza mreza;
                List<Mreza> result = em.createQuery(
                        "SELECT m FROM Mreza m WHERE m.naziv = :naziv", Mreza.class)
                        .setParameter("naziv", naziv)
                        .getResultList();

                if (result.isEmpty()) {
                    mreza = new Mreza();
                    mreza.setNaziv(naziv);
                    mreza.setNazivEng(mrezaJson.getString("nazivEng", ""));
                    mreza.setZadnjaPromjena(Timestamp.from(Instant.now()));
                    mreza.setPostaje(new ArrayList<>());
                    //JPA generira insert 
                    em.persist(mreza);
                    //slanje upita
                    em.flush();
                } else {
                    mreza = result.get(0);
                    mreza.setNazivEng(mrezaJson.getString("nazivEng", ""));
                    mreza.setZadnjaPromjena(Timestamp.from(Instant.now()));
                    if (mreza.getPostaje() == null) {
                        mreza.setPostaje(new ArrayList<>());
                    }
                }

              
                JsonArray postajeArray = mrezaJson.getJsonArray("postaje");
                if (postajeArray != null) {
                    for (JsonValue pval : postajeArray) {
                        JsonObject pJson = pval.asJsonObject();
                        String nazivPostaje = pJson.getString("naziv");
                        Postaja postaja;
                        List<Postaja> posts = em.createQuery(
                                "SELECT p FROM Postaja p WHERE p.naziv = :naziv AND p.mreza = :mreza", Postaja.class)
                                .setParameter("naziv", nazivPostaje)
                                .setParameter("mreza", mreza)
                                .getResultList();

                        if (posts.isEmpty()) {
                            postaja = new Postaja();
                            postaja.setNaziv(nazivPostaje);
                            postaja.setNazivEng(pJson.getString("nazivEng", ""));
                            postaja.setAktivna(pJson.getBoolean("aktivan", false));
                            postaja.setZadnjaPromjena(Timestamp.from(Instant.now()));
                            postaja.setMreza(mreza);
                             //JPA generira insert 
                            em.persist(postaja);
                            //slanje upita
                            em.flush();
                            mreza.getPostaje().add(postaja);
                        } else {
                            postaja = posts.get(0);
                            postaja.setNazivEng(pJson.getString("nazivEng", ""));
                            postaja.setAktivna(pJson.getBoolean("aktivan", false));
                            postaja.setZadnjaPromjena(Timestamp.from(Instant.now()));
                            if (!mreza.getPostaje().contains(postaja)) {
                                mreza.getPostaje().add(postaja);
                            }
                        }
                    }
                }
            }

            reader.close();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom sync-a mreža", e);
        }
    }

  
    public Mreza getMrezaByNaziv(String naziv) {
        return em.createQuery(
                "SELECT m FROM Mreza m WHERE m.naziv = :naziv", Mreza.class)
                .setParameter("naziv", naziv)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

 
    public MrezaDTO getMrezaDTOByNaziv(String naziv) {
        Mreza mreza = getMrezaByNaziv(naziv);
        if (mreza == null)
            return null;

        List<PostajaDTO> postajeDto = mreza.getPostaje().stream()
                .map(p -> new PostajaDTO(p.getNaziv(), p.getNazivEng(), p.getAktivna(), p.getMreza().getNaziv()))
                .toList();

        return new MrezaDTO(mreza.getNaziv(), mreza.getNazivEng(), postajeDto);
    }


    public List<MrezaDTO> getAllMrezeDTO() {
        List<Mreza> mreze = em.createQuery("SELECT m FROM Mreza m", Mreza.class).getResultList();

        return mreze.stream()
                .map(m -> {
                    List<PostajaDTO> postajeDto = m.getPostaje().stream()
                            .map(p -> new PostajaDTO(p.getNaziv(), p.getNazivEng(), p.getAktivna(),
                                    p.getMreza().getNaziv()))
                            .toList();
                    return new MrezaDTO(m.getNaziv(), m.getNazivEng(), postajeDto);
                })
                .toList();
    }

    public List<MrezaSimpleDTO> getAllMrezeSimple() {
        return em.createQuery("SELECT m FROM Mreza m", Mreza.class)
                .getResultList()
                .stream()
                .map(m -> new MrezaSimpleDTO(m.getNaziv(), m.getNazivEng()))
                .toList();
    }

    public PostajaDTO getPostajaDTOByNaziv(String naziv) {
        Postaja postaja = em.createQuery(
                "SELECT p FROM Postaja p WHERE p.naziv = :naziv", Postaja.class)
                .setParameter("naziv", naziv)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (postaja == null)
            return null;

        return new PostajaDTO(postaja.getNaziv(), postaja.getNazivEng(), postaja.getAktivna(),
                postaja.getMreza().getNaziv());
    }

    public PostajaDTO getPostajaDTOByNazivAndMreza(String naziv, String mrezaNaziv) {
        Postaja postaja = em.createQuery(
                "SELECT p FROM Postaja p WHERE p.naziv = :naziv AND p.mreza.naziv = :mrezaNaziv", Postaja.class)
                .setParameter("naziv", naziv)
                .setParameter("mrezaNaziv", mrezaNaziv)
                .getResultStream()
                .findFirst()
                .orElse(null);
    
        if (postaja == null) return null;
    
        return new PostajaDTO(
                postaja.getNaziv(),
                postaja.getNazivEng(),
                postaja.getAktivna(),
                postaja.getMreza().getNaziv()
        );
    }
    

    public boolean updatePostaja(String naziv, String nazivEng, boolean aktivna) {
        Postaja postaja = em.createQuery(
                "SELECT p FROM Postaja p WHERE p.naziv = :naziv", Postaja.class)
                .setParameter("naziv", naziv)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (postaja == null)
            return false;

        postaja.setNazivEng(nazivEng);
        postaja.setAktivna(aktivna);
        em.merge(postaja);
        return true;
    }

}
