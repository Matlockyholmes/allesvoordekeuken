package be.vdab.allesvoordekeuken.repositories;

import be.vdab.allesvoordekeuken.domain.*;
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

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaArtikelRepository.class)
@Sql("/insertArtikelGroep.sql")
@Sql("/insertArtikel.sql")
public class JpaArtikelRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private JpaArtikelRepository repository;
    private FoodArtikel foodArtikel;
    private NonFoodArtikel nonFoodArtikel;
    @Autowired
    private EntityManager manager;
    private ArtikelGroep artikelGroep;

    private final static String ARTIKELS = "artikels";

    @Before()
    public void before(){
        artikelGroep = new ArtikelGroep("test");
        foodArtikel = new FoodArtikel("salami", BigDecimal.ONE, BigDecimal.ONE, 5, artikelGroep);
        nonFoodArtikel = new NonFoodArtikel("bezem", BigDecimal.TEN, BigDecimal.TEN, 7, artikelGroep);
    }

    public long idVanTestArtikel(){
        return super.jdbcTemplate.queryForObject("select id from artikels where naam='test'", Long.class);
    }

    public long idVanTestFoodArtikel(){
        return super.jdbcTemplate.queryForObject("select id from artikels where naam = 'testFoodArtikel'", Long.class);
    }

    public long idVanTestNonFoodArtikel(){
        return super.jdbcTemplate.queryForObject("select id from artikels where naam = 'testNonFoodArtikel'", Long.class);
    }

    @Test
    public void findById(){
        assertThat(repository.findById(idVanTestArtikel()).get().getNaam()).isEqualTo("test");
    }

    @Test
    public void findFoodById(){
        assertThat(((FoodArtikel) repository.findById(idVanTestFoodArtikel()).get()).getHoudbaarheid()).isEqualTo(69);
    }

    @Test
    public void findNonFoodById(){
        assertThat(((NonFoodArtikel) repository.findById(idVanTestNonFoodArtikel()).get()).getGarantie()).isEqualTo(420);
    }
    @Test
    public void findByOnbestaandeId(){
        assertThat(repository.findById(-1L)).isNotPresent();
    }

    @Test
    public void findByNaam(){
        List<Artikel> artikels = repository.findByNaam("es");
        manager.clear();
        assertThat(artikels)
                .hasSize(super.jdbcTemplate.queryForObject(
                        "select count(*) from artikels where naam like '%es%'", Integer.class))
                .extracting(artikel -> artikel.getNaam().toLowerCase())
                .allSatisfy(artikel -> assertThat(artikel).contains("es")).isSorted();
        assertThat(artikels).extracting(artikel->artikel.getArtikelGroep().getNaam());
    }

    @Test
    public void algemeneVerhoging(){
        assertThat(repository.algemeneVerhoging(BigDecimal.TEN)).isEqualTo(super.countRowsInTable(ARTIKELS));
        assertThat(super.jdbcTemplate.queryForObject("select verkoopprijs from artikels where id=?", BigDecimal.class, idVanTestArtikel())).isEqualByComparingTo("1.1");
    }

    @Test
    public void createFoodArtikel(){
        manager.persist(artikelGroep);
        repository.create(foodArtikel);
        assertThat(super.countRowsInTableWhere(ARTIKELS, "id=" + idVanTestFoodArtikel())).isOne();
        assertThat(super.jdbcTemplate.queryForObject("select artikelgroepid from artikels where id=?", Long.class, foodArtikel.getId())).isEqualTo(artikelGroep.getId());
    }

    @Test
    public void createNonFoodArtikel(){
        manager.persist(artikelGroep);
        repository.create(nonFoodArtikel);
        assertThat(super.countRowsInTableWhere(ARTIKELS, "id=" + idVanTestNonFoodArtikel())).isOne();
        assertThat(super.jdbcTemplate.queryForObject("select artikelgroepid from artikels where id=?", Long.class, nonFoodArtikel.getId())).isEqualTo(artikelGroep.getId());

    }
    @Test
    public void kortingenLezen(){
        assertThat(repository.findById(idVanTestArtikel()).get().getKortingen()).containsOnly(new Korting(1, BigDecimal.ONE));
    }
    @Test
    public void artikelGroepLazyLoaded(){
        assertThat(repository.findById(idVanTestFoodArtikel()).get().getArtikelGroep().getNaam()).isEqualTo("test");
    }
}
