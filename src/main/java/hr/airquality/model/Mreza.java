package hr.airquality.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "mreza")
public class Mreza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String naziv;

    @Column(name = "naziv_eng")
    private String nazivEng;

    @Column(name = "zadnja_promjena")
    private java.sql.Timestamp zadnjaPromjena;

    @OneToMany(mappedBy = "mreza", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Postaja> postaje;

   

    public Mreza(String naziv) {
        this.naziv = naziv;
    }
}
