package hr.airquality.repository;

import hr.airquality.model.Mreza;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@Stateless
public class MrezaRepository {

    @PersistenceContext
    private EntityManager em;

    public Optional<Mreza> findByNaziv(String naziv) {
        return em.createQuery(
                "SELECT m FROM Mreza m WHERE m.naziv = :naziv", Mreza.class)
                .setParameter("naziv", naziv)
                .getResultStream()
                .findFirst();
    }

    public List<Mreza> findAll() {
        return em.createQuery(
                "SELECT m FROM Mreza m", Mreza.class)
                .getResultList();
    }

    public Mreza save(Mreza mreza) {
        em.persist(mreza);
        return mreza;
    }

    public Mreza update(Mreza mreza) {
        return em.merge(mreza);
    }
}
