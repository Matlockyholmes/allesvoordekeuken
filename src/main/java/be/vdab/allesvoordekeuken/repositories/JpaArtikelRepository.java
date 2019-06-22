package be.vdab.allesvoordekeuken.repositories;

import be.vdab.allesvoordekeuken.domain.Artikel;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaArtikelRepository implements ArtikelRepository {

    private final EntityManager manager;

    JpaArtikelRepository(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public Optional<Artikel> findById(long id) {
        return Optional.ofNullable(manager.find(Artikel.class, id));
    }

    @Override
    public List<Artikel> findByNaam(String woord) {
        return manager.createNamedQuery("Artikel.findByNaam", Artikel.class)
                .setParameter("naamfragment", '%' + woord + '%').getResultList();
    }

    @Override
    public int algemeneVerhoging(BigDecimal percentage) {
        BigDecimal factor = BigDecimal.ONE.add(percentage.divide(BigDecimal.valueOf(100)));
        return manager.createNamedQuery("Artikel.algemeneVerhoging")
                .setParameter("factor", factor)
                .executeUpdate();
    }
}
