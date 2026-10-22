package br.edu.ifto.clinica.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data e hora são obrigatórias")
    @FutureOrPresent(message = "A data da consulta não pode estar no passado")
    private LocalDateTime data;

    @NotNull(message = "O valor da consulta é obrigatório")
    @Positive(message = "O valor da consulta deve ser maior que zero")
    private Double valor;

    private String observacao;

    @ManyToOne
    @NotNull(message = "O paciente é obrigatório")
    private Paciente paciente;

    @ManyToOne
    @NotNull(message = "O médico é obrigatório")
    private Medico medico;

    public Consulta() {
    }

    public Consulta(LocalDateTime data, Double valor, String observacao,
                    Paciente paciente, Medico medico) {
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

    public String dados() {
        return "Data: " + data
                + ", Valor: " + valor
                + ", Observação: " + observacao;
    }
}