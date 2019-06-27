package be.vdab.allesvoordekeuken.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "artikelgroepen")
public class ArtikelGroep implements Serializable {
    private static final long SerialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String naam;
    @OneToMany(mappedBy = "artikelGroep")
    @OrderBy("naam")
    private Set<Artikel> artikels;

    public ArtikelGroep() {
    }

    public ArtikelGroep(String naam) {
        this.naam = naam;
        this.artikels = new LinkedHashSet<>();
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public Set<Artikel> getArtikels() {
        return Collections.unmodifiableSet(artikels);
    }

    public boolean add(Artikel artikel) {
        boolean toegevoegd = artikels.add(artikel);
        ArtikelGroep oudeArtikelGroep = artikel.getArtikelGroep();
        if (oudeArtikelGroep != null && oudeArtikelGroep != this){
            oudeArtikelGroep.artikels.remove(artikel);
        }
        if(this != oudeArtikelGroep){
            artikel.setArtikelGroep(this);
        }
        return toegevoegd;
    }
}
