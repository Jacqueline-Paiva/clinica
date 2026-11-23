package br.edu.ifto.clinica.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifto.clinica.model.Especialidade;

public interface EspecialidadeRepository
        extends JpaRepository<Especialidade, Long> {

    Optional<Especialidade> findByNome(String nome);
}