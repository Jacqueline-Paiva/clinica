package br.edu.ifto.clinica.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifto.clinica.model.Agenda;
import br.edu.ifto.clinica.model.Medico;
import br.edu.ifto.clinica.model.StatusAgenda;

public interface AgendaRepository
        extends JpaRepository<Agenda, Long> {

    List<Agenda> findByStatusOrderByDataAscHorarioAsc(
            StatusAgenda status
    );

    List<Agenda> findByMedicoOrderByDataAscHorarioAsc(
            Medico medico
    );

    boolean existsByMedicoAndDataAndHorario(
            Medico medico,
            LocalDate data,
            LocalTime horario
    );
}