package br.edu.ifto.clinica.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data e hora são obrigatórias")
    private LocalDateTime data;

    @Positive(message = "O valor da consulta deve ser maior que zero")
    private Double valor;

    private String observacao;

    @ManyToOne
    @NotNull(message = "O paciente é obrigatório")
    private Paciente paciente;

    @ManyToOne
    @NotNull(message = "O médico é obrigatório")
    private Medico medico;

    @OneToOne
    @JoinColumn(name = "agendamento_id", unique = true)
    private Agendamento agendamento;

    public Consulta() {
    }

    public Consulta(
            LocalDateTime data,
            Double valor,
            String observacao,
            Paciente paciente,
            Medico medico) {

        this.data = data;
        this.valor = valor;
        this.observacao = observacao;
        this.paciente = paciente;
        this.medico = medico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public String dados() {
        return "Data: " + data
                + ", Valor: " + valor
                + ", Observação: " + observacao;
    }
}