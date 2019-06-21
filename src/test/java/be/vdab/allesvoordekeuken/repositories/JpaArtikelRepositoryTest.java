package be.vdab.allesvoordekeuken.repositories;

import be.vdab.allesvoordekeuken.domain.Artikel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaArtikelRepository.class)
@Sql("/insertArtikel.sql")
public class JpaArtikelRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String ARTIKELS = "artikels";
    private Artikel artikel;
    @Autowired
    private JpaArtikelRepository repository;

    public long idVanTestArtikel(){
        return super.jdbcTemplate.queryForObject("select id from artikels where naam='test'", Long.class);
    }

    @Before
    public void before(){
        artikel = new Artikel("testArtikel", BigDecimal.ONE, BigDecimal.ONE);
    }
    @Test
    public void findById(){
        assertThat(repository.findById(idVanTestArtikel()).get().getNaam()).isEqualTo("test");
    }

    @Test
    public void findByOnbestaandeId(){
        assertThat(repository.findById(-1L)).isNotPresent();
    }

    @Test
    public void createArtikelTest(){
        repository.create(artikel);
        assertThat(artikel.getId()).isPositive();
        assertThat(super.countRowsInTableWhere(ARTIKELS, "id= " + artikel.getId())).isOne();
    }
}
