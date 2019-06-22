package be.vdab.allesvoordekeuken.repositories;

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

    private final static String ARTIKELS = "artikels";

    public long idVanTestArtikel(){
        return super.jdbcTemplate.queryForObject("select id from artikels where naam='test'", Long.class);
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
}
