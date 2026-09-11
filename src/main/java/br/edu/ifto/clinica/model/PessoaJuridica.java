package br.edu.ifto.clinica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class PessoaJuridica extends Pessoa {

    private String razaoSocial;
    private String cnpj;

    public PessoaJuridica() {
    }

    public PessoaJuridica(String email, String telefone, String razaoSocial, String cnpj) {
        super(email, telefone);
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}