package hr.airquality.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "postaja")
public class Postaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String naziv;

    @Column(name = "naziv_eng")
    private String nazivEng;

    private Boolean aktivna;

    @Column(name = "zadnja_promjena")
    private Timestamp zadnjaPromjena;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mreza_id")
    private Mreza mreza;



    public Postaja(String naziv, Mreza mreza) {
        this.naziv = naziv;
        this.mreza = mreza;
    }
}
