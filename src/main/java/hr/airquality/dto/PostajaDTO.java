package hr.airquality.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostajaDTO {
    private String naziv;
    private String nazivEng;
    private boolean aktivna;
    private String mrezaNaziv;

    public PostajaDTO() {}

    public PostajaDTO(String naziv, String nazivEng, boolean aktivna, String mrezaNaziv) {
            this.naziv = naziv;
            this.nazivEng = nazivEng;
            this.aktivna = aktivna;
            this.mrezaNaziv = mrezaNaziv;
    }
}
