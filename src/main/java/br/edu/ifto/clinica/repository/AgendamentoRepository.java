package br.edu.ifto.clinica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifto.clinica.model.Agenda;
import br.edu.ifto.clinica.model.Agendamento;
import br.edu.ifto.clinica.model.Medico;
import br.edu.ifto.clinica.model.Paciente;
import br.edu.ifto.clinica.model.StatusAgendamento;

public interface AgendamentoRepository
        extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findAllByOrderByIdDesc();

    List<Agendamento> findByPacienteOrderByIdDesc(
            Paciente paciente
    );

    List<Agendamento> findByAgendaMedicoOrderByIdDesc(
            Medico medico
    );

    List<Agendamento> findByStatusOrderByIdDesc(
            StatusAgendamento status
    );

    boolean existsByAgendaAndStatus(
            Agenda agenda,
            StatusAgendamento status
    );
}