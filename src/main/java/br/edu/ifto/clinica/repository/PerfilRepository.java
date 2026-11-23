package br.edu.ifto.clinica.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifto.clinica.model.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Optional<Perfil> findByNome(String nome);
}