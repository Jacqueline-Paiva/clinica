package br.edu.ifto.clinica.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifto.clinica.model.Paciente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class PacienteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Paciente paciente) {
        entityManager.persist(paciente);
    }

    public List<Paciente> findAll() {
        return entityManager
                .createQuery(
                        "SELECT p FROM Paciente p ORDER BY p.nome ASC",
                        Paciente.class)
                .getResultList();
    }

    public Paciente findById(Long id) {
        return entityManager.find(Paciente.class, id);
    }

    public Paciente findByUsuario(String usuario) {

        List<Paciente> pacientes = entityManager
                .createQuery(
                        "SELECT p FROM Paciente p " +
                        "WHERE p.usuario.usuario = :usuario",
                        Paciente.class)
                .setParameter("usuario", usuario)
                .getResultList();

        if (pacientes.isEmpty()) {
            return null;
        }

        return pacientes.get(0);
    }

    public List<Paciente> buscar(String termo) {

    return entityManager
            .createQuery(
                    """
                    SELECT p
                    FROM Paciente p
                    WHERE LOWER(p.nome) LIKE LOWER(:termo)
                       OR p.cpf LIKE :termo
                    ORDER BY p.nome
                    """,
                    Paciente.class)
            .setParameter("termo", termo.trim() + "%")
            .getResultList();
    }

    @Transactional
    public void update(Paciente paciente) {
        entityManager.merge(paciente);
    }

    @Transactional
    public void delete(Long id) {

        Paciente paciente = findById(id);

        if (paciente != null) {
            entityManager.remove(paciente);
        }
    }
}