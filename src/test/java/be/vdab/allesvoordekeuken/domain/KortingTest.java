package be.vdab.allesvoordekeuken.domain;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class KortingTest {
    private Korting korting1, nogEensKorting1, korting2;
    @Before
    public void before(){
        korting1 = new Korting(1, BigDecimal.ONE);
        nogEensKorting1 = new Korting(1, BigDecimal.ONE);
        korting2 = new Korting(2, BigDecimal.ONE);
    }

    @Test
    public void kortingIsGelijkAlsHunVanafaantalGelijkIs(){
        assertThat(korting1).isEqualTo(nogEensKorting1);
    }

    @Test
    public void kortingVerschiltVanNull(){
        assertThat(korting1).isNotEqualTo(null);
    }

    @Test
    public void kortingVerschiltVanAnderTypeObject(){
        assertThat(korting1).isNotEqualTo("");
    }

    @Test
    public void kortingIsVerschillendMetAnderVanafAantal(){
        assertThat(korting1).isNotEqualTo(korting2);
    }
    @Test
    public void gelijkeKortingGeeftGelijkeHashCode(){
        assertThat(korting1).hasSameHashCodeAs(nogEensKorting1);
    }

}
