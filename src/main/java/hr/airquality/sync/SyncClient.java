package hr.airquality.sync;

import jakarta.ejb.Stateless;
import jakarta.json.*;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Stateless
public class SyncClient {

    private static final String API_URL =
            "http://www.ekonerg.hr/iskzl/rs/mreza/list";

    public JsonArray fetchMreze() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (JsonReader reader = Json.createReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                return reader.readArray();
            } finally {
                conn.disconnect();
            }

        } catch (Exception e) {
            throw new RuntimeException("Greška pri pozivu Ekonerg API-ja", e);
        }
    }
}
