package hr.airquality.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MrezaDTO {
    private String naziv;
    private String nazivEng;
    private List<PostajaDTO> postaje;

    public MrezaDTO() {}

    public MrezaDTO(String naziv, String nazivEng, List<PostajaDTO> postaje) {
        this.naziv = naziv;
        this.nazivEng = nazivEng;
        this.postaje = postaje;
    }
    
}
