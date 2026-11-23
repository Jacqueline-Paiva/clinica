package br.edu.ifto.clinica.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Medico extends PessoaFisica {

    @NotBlank(message = "O CRM é obrigatório")
    private String crm;

    @NotNull(message = "A especialidade é obrigatória")
    @ManyToOne
    @JoinColumn(name = "especialidade_id")
    private Especialidade especialidade;

    @OneToMany(mappedBy = "medico")
    private List<Consulta> consultas;

    public Medico() {
    }

    public Medico(
            String email,
            String telefone,
            String nome,
            String cpf,
            String crm) {

        super(email, telefone, nome, cpf);
        this.crm = crm;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
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