package hr.airquality.sync;

import hr.airquality.model.Mreza;
import hr.airquality.model.Postaja;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonObject;

@ApplicationScoped
public class SyncMapper {

    public void updateMreza(Mreza mreza, JsonObject json) {
        mreza.setNazivEng(json.getString("nazivEng", ""));
    }

    public Postaja toPostaja(JsonObject json, Mreza mreza) {
        Postaja p = new Postaja(json.getString("naziv"), mreza);
        p.setNaziv(json.getString("naziv"));
        p.setNazivEng(json.getString("nazivEng", ""));
        p.setAktivna(json.getBoolean("aktivan", false));
        p.setMreza(mreza);
        return p;
    }

    public void updatePostaja(Postaja postaja, JsonObject json) {
        postaja.setNazivEng(json.getString("nazivEng", ""));
        postaja.setAktivna(json.getBoolean("aktivan", false));
    }
}
