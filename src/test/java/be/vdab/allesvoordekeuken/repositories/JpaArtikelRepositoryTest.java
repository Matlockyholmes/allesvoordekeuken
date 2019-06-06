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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaArtikelRepository.class)
@Sql("/insertArtikel.sql")
public class JpaArtikelRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private JpaArtikelRepository repository;

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
}
