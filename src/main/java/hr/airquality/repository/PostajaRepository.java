package hr.airquality.repository;

import hr.airquality.model.Mreza;
import hr.airquality.model.Postaja;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@Stateless
public class PostajaRepository {

    @PersistenceContext
    private EntityManager em;

    public Optional<Postaja> findByNazivAndMreza(String naziv, Mreza mreza) {
        return em.createQuery(
                "SELECT p FROM Postaja p WHERE p.naziv = :naziv AND p.mreza = :mreza", Postaja.class)
                .setParameter("naziv", naziv)
                .setParameter("mreza", mreza)
                .getResultStream()
                .findFirst();
    }

    public Optional<Postaja> findByNaziv(String naziv) {
        return em.createQuery(
                "SELECT p FROM Postaja p WHERE p.naziv = :naziv", Postaja.class)
                .setParameter("naziv", naziv)
                .getResultStream()
                .findFirst();
    }

    public List<Postaja> findAll() {
        return em.createQuery("SELECT p FROM Postaja p", Postaja.class)
                .getResultList();
    }

    public Postaja save(Postaja postaja) {
        em.persist(postaja);
        return postaja;
    }

    public Postaja update(Postaja postaja) {
        return em.merge(postaja);
    }
}
