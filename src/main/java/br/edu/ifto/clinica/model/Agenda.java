package br.edu.ifto.clinica.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"medico_id", "data", "horario"}
        )
    }
)
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O médico é obrigatório")
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @NotNull(message = "A data é obrigatória")
    @FutureOrPresent(message = "A data não pode estar no passado")
    private LocalDate data;

    @NotNull(message = "O horário é obrigatório")
    private LocalTime horario;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusAgenda status = StatusAgenda.DISPONIVEL;

    public Agenda() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public StatusAgenda getStatus() {
        return status;
    }

    public void setStatus(StatusAgenda status) {
        this.status = status;
    }
}