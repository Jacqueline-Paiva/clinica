package br.edu.ifto.clinica.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.validation.constraints.NotBlank;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Medico extends PessoaFisica {

    @NotBlank(message = "O CRM é obrigatório")
    private String crm;

    @OneToMany(mappedBy = "medico")
    private List<Consulta> consultas;

    public Medico() {
    }

    public Medico(String email, String telefone, String nome, String cpf, String crm) {
        super(email, telefone, nome, cpf);
        this.crm = crm;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    public String dados() {
        return "Nome: " + getNome() + ", CRM: " + crm;
    }

    public String consultas() {
        return "Quantidade de consultas: " + consultas.size();
    }
}