package br.edu.ifto.clinica.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Paciente extends PessoaFisica {

    @OneToMany(mappedBy = "paciente")
    private List<Consulta> consultas;

    public Paciente() {
    }

    public Paciente(String email, String telefone, String nome, String cpf) {
        super(email, telefone, nome, cpf);
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    public String dados() {
        return "Nome: " + getNome() + ", Telefone: " + getTelefone();
    }

    public String consultas() {
        return "Quantidade de consultas: " + consultas.size();
    }
}