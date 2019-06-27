package be.vdab.allesvoordekeuken.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "artikels")
@DiscriminatorColumn(name = "soort")
@NamedEntityGraph(name = "Artikel.OP_NAAM", attributeNodes = @NamedAttributeNode("artikelGroep"))
public abstract class Artikel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String naam;
    private BigDecimal aankoopprijs;
    private BigDecimal verkoopprijs;
    @ElementCollection
    @CollectionTable(name = "kortingen", joinColumns = @JoinColumn(name = "artikelId"))
    @OrderBy("percentage")
    private Set<Korting> kortingen;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artikelgroepid")
    private ArtikelGroep artikelGroep;
    private static final String OP_NAAM = "Artikel.opNaam";

    protected Artikel() {
    }

    public Artikel(String naam, BigDecimal aankoopprijs, BigDecimal verkoopprijs, ArtikelGroep artikelGroep) {
        this.naam = naam;
        this.aankoopprijs = aankoopprijs;
        this.verkoopprijs = verkoopprijs;
        this.kortingen = new LinkedHashSet<>();
        setArtikelGroep(artikelGroep);
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public BigDecimal getAankoopprijs() {
        return aankoopprijs;
    }

    public BigDecimal getVerkoopprijs() {
        return verkoopprijs;
    }

    public Set<Korting> getKortingen() {
        return Collections.unmodifiableSet(kortingen);
    }

    public ArtikelGroep getArtikelGroep() {
        return artikelGroep;
    }

    public void setArtikelGroep(ArtikelGroep artikelGroep) {
        if(!artikelGroep.getArtikels().contains(this)){
            artikelGroep.add(this);
        }
        this.artikelGroep = artikelGroep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artikel)) return false;
        Artikel artikel = (Artikel) o;
        return Objects.equals(naam, artikel.naam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(naam);
    }
}
