package hr.airquality.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MrezaSimpleDTO {

    private String naziv;
    private String nazivEng;

    public MrezaSimpleDTO() {}

    public MrezaSimpleDTO(String naziv, String nazivEng) {
        this.naziv = naziv;
        this.nazivEng = nazivEng;
    }

}
