package be.vdab.allesvoordekeuken.domain;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

public class ArtikelGroepTest {
    private Artikel artikel1;
    private ArtikelGroep artikelGroep1;
    private ArtikelGroep artikelGroep2;

    @Before
    public void before(){
        artikelGroep1 = new ArtikelGroep("test1");
        artikelGroep2 = new ArtikelGroep("test2");
        artikel1 = new FoodArtikel("test1", BigDecimal.ONE, BigDecimal.ONE, 1, artikelGroep1);
    }

    @Test
    public void artikelGroep1IsDeArtikgelGroepVanArtikel1(){
        assertThat(artikel1.getArtikelGroep()).isEqualTo(artikelGroep1);
        assertThat(artikelGroep1.getArtikels()).containsOnly(artikel1);
    }

    @Test
    public void artikel1VerhuistNaarArtikelGroep2(){
        assertThat(artikelGroep2.add(artikel1)).isTrue();
        assertThat(artikelGroep1.getArtikels()).isEmpty();
        assertThat(artikelGroep2.getArtikels()).containsOnly(artikel1);
        assertThat(artikel1.getArtikelGroep()).isEqualTo(artikelGroep2);
    }
    @Test
    public void nullAlsArtikelGroepToevoegenMislukt(){
        assertThatNullPointerException().isThrownBy(()-> artikelGroep1.add(null));
    }
}
