package be.vdab.allesvoordekeuken.domain;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

public class ArtikelTest {
    private Artikel artikel1;
    private Artikel artikel2;
    private ArtikelGroep artikelGroep1;
    private ArtikelGroep artikelGroep2;

    @Before
    public void before(){
        artikelGroep1 = new ArtikelGroep("test1");
        artikelGroep2 = new ArtikelGroep("test2");
        artikel1 = new FoodArtikel("test1", BigDecimal.ONE, BigDecimal.ONE, 1, artikelGroep1);
        artikel2 = new NonFoodArtikel("test2", BigDecimal.ONE, BigDecimal.ONE, 1, artikelGroep1);
    }

    @Test
    public void artikel1KomtVoorInArtikelGroep1(){
        assertThat(artikel1.getArtikelGroep()).isEqualTo(artikelGroep1);
        assertThat(artikelGroep1.getArtikels()).contains(artikel1);
    }

    @Test
    public void artikel1VerhuisNaarArtikelGroep2(){
        artikel1.setArtikelGroep(artikelGroep2);
        assertThat(artikel1.getArtikelGroep()).isEqualTo(artikelGroep2);
        assertThat(artikelGroep2.getArtikels()).containsOnly(artikel1);
        assertThat(artikelGroep1.getArtikels()).containsOnly(artikel2);
    }

    @Test
    public void eenNullArtikelGroepInDeSetterMislukt(){
        assertThatNullPointerException().isThrownBy(()-> artikel1.setArtikelGroep(null));
    }
}
