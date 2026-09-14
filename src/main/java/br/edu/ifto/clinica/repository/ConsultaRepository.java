package br.edu.ifto.clinica.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifto.clinica.model.Consulta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ConsultaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Consulta consulta) {
        entityManager.persist(consulta);
    }

    public List<Consulta> findAll() {
        return entityManager
                .createQuery(
                        "SELECT c FROM Consulta c ORDER BY c.data ASC",
                        Consulta.class)
                .getResultList();
    }

    public Consulta findById(Long id) {
        return entityManager.find(Consulta.class, id);
    }

    public List<Consulta> findByMedico(Long medicoId) {
        return entityManager
                .createQuery(
                        "SELECT c FROM Consulta c WHERE c.medico.id = :medicoId ORDER BY c.data ASC",
                        Consulta.class)
                .setParameter("medicoId", medicoId)
                .getResultList();
    }

    public List<Consulta> findByPaciente(Long pacienteId) {
        return entityManager
                .createQuery(
                        "SELECT c FROM Consulta c WHERE c.paciente.id = :pacienteId ORDER BY c.data ASC",
                        Consulta.class)
                .setParameter("pacienteId", pacienteId)
                .getResultList();
    }

    @Transactional
    public void update(Consulta consulta) {
        entityManager.merge(consulta);
    }

    @Transactional
    public void delete(Long id) {
        Consulta consulta = findById(id);

        if (consulta != null) {
            entityManager.remove(consulta);
        }
    }
}