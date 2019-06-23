package be.vdab.allesvoordekeuken.repositories;

import be.vdab.allesvoordekeuken.domain.FoodArtikel;
import be.vdab.allesvoordekeuken.domain.NonFoodArtikel;
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
    @Autowired
    private JpaArtikelRepository repository;
    private FoodArtikel foodArtikel;
    private NonFoodArtikel nonFoodArtikel;

    private final static String ARTIKELS = "artikels";

    @Before()
    public void before(){
        foodArtikel = new FoodArtikel("salami", BigDecimal.ONE, BigDecimal.ONE, 5);
        nonFoodArtikel = new NonFoodArtikel("bezem", BigDecimal.TEN, BigDecimal.TEN, 7);
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
    public void findByNonFoodId(){

    }
    @Test
    public void findByOnbestaandeId(){
        assertThat(repository.findById(-1L)).isNotPresent();
    }

    @Test
    public void findByNaam(){
        assertThat(repository.findByNaam("es")).hasSize(super.countRowsInTableWhere(ARTIKELS, "naam like '%es%'"))
                .extracting(artikel -> artikel.getNaam().toLowerCase())
                .allSatisfy(naam -> assertThat(naam).contains("es"))
                .isSorted();
    }

    @Test
    public void algemeneVerhoging(){
        assertThat(repository.algemeneVerhoging(BigDecimal.TEN)).isEqualTo(super.countRowsInTable(ARTIKELS));
        assertThat(super.jdbcTemplate.queryForObject("select verkoopprijs from artikels where id=?", BigDecimal.class, idVanTestArtikel())).isEqualByComparingTo("1.1");
    }

    @Test
    public void createFoodArtikel(){
        repository.create(foodArtikel);
        assertThat(super.countRowsInTableWhere(ARTIKELS, "id=" + idVanTestFoodArtikel())).isOne();
    }

    @Test
    public void createNonFoodArtikel(){
        repository.create(nonFoodArtikel);
        assertThat(super.countRowsInTableWhere(ARTIKELS, "id=" + idVanTestNonFoodArtikel())).isOne();
    }
}
