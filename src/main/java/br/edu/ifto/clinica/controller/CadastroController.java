package br.edu.ifto.clinica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifto.clinica.model.Paciente;
import br.edu.ifto.clinica.model.Perfil;
import br.edu.ifto.clinica.model.Usuario;
import br.edu.ifto.clinica.repository.PacienteRepository;
import br.edu.ifto.clinica.repository.PerfilRepository;
import br.edu.ifto.clinica.repository.UsuarioRepository;
import jakarta.validation.Valid;

@Controller
public class CadastroController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/cadastro")
    public String cadastro(Model model) {

        Paciente paciente = new Paciente();
        paciente.setUsuario(new Usuario());

        model.addAttribute("paciente", paciente);

        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(
            @Valid Paciente paciente,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (paciente.getUsuario() != null
                && usuarioRepository
                    .findByUsuario(paciente.getUsuario().getUsuario())
                    .isPresent()) {

            result.rejectValue(
                    "usuario.usuario",
                    "usuario.duplicado",
                    "Este nome de usuário já está cadastrado"
            );
        }

        if (result.hasErrors()) {
            return "cadastro";
        }

        Perfil perfilPaciente = perfilRepository
                .findByNome("PACIENTE")
                .orElseThrow(() ->
                        new RuntimeException(
                                "Perfil PACIENTE não encontrado"
                        ));

        Usuario usuario = paciente.getUsuario();

        usuario.setSenha(
                passwordEncoder.encode(usuario.getSenha())
        );

        usuario.getPerfis().add(perfilPaciente);

        pacienteRepository.save(paciente);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Cadastro realizado com sucesso. Faça login para continuar."
        );

        return "redirect:/login";
    }
}