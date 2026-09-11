package br.edu.ifto.clinica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class PessoaFisica extends Pessoa {

    private String nome;
    private String cpf;

    public PessoaFisica() {
    }

    public PessoaFisica(String email, String telefone, String nome, String cpf) {
        super(email, telefone);
        this.nome = nome;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
