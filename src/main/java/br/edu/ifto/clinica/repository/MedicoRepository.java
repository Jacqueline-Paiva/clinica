package br.edu.ifto.clinica.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifto.clinica.model.Medico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class MedicoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Medico medico) {
        entityManager.persist(medico);
    }

    public List<Medico> findAll() {
    return entityManager
        .createQuery("SELECT m FROM Medico m ORDER BY m.nome ASC", Medico.class)
        .getResultList();
    }

    public Medico findById(Long id) {
        return entityManager.find(Medico.class, id);
    }

    @Transactional
    public void update(Medico medico) {
        entityManager.merge(medico);
    }

    @Transactional
    public void delete(Long id) {
        Medico medico = findById(id);

        if (medico != null) {
            entityManager.remove(medico);
        }
    }
}