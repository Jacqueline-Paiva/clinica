package br.edu.ifto.clinica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifto.clinica.model.Paciente;
import br.edu.ifto.clinica.model.Perfil;
import br.edu.ifto.clinica.model.Usuario;
import br.edu.ifto.clinica.repository.AgendamentoRepository;
import br.edu.ifto.clinica.repository.ConsultaRepository;
import br.edu.ifto.clinica.repository.PacienteRepository;
import br.edu.ifto.clinica.repository.PerfilRepository;
import br.edu.ifto.clinica.repository.UsuarioRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listar(
            @RequestParam(required = false) String busca,
            Model model) {

        boolean pesquisou =
                busca != null && !busca.isBlank();

        if (pesquisou) {

            model.addAttribute(
                    "pacientes",
                    pacienteRepository.buscar(busca.trim())
            );

        } else {

            model.addAttribute(
                    "pacientes",
                    List.of()
            );
        }

        model.addAttribute("busca", busca);
        model.addAttribute("pesquisou", pesquisou);

        return "paciente/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {

        Paciente paciente = new Paciente();
        paciente.setUsuario(new Usuario());

        model.addAttribute("paciente", paciente);

        return "paciente/form";
    }

    @PostMapping("/salvar")
    public String salvar(
            @Valid Paciente paciente,
            BindingResult result) {

        if (paciente.getUsuario() != null
                && usuarioRepository
                    .findByUsuario(
                            paciente.getUsuario().getUsuario()
                    )
                    .isPresent()) {

            result.rejectValue(
                    "usuario.usuario",
                    "usuario.duplicado",
                    "Este nome de usuário já está cadastrado"
            );
        }

        if (result.hasErrors()) {
            return "paciente/form";
        }

        Perfil perfilPaciente = perfilRepository
                .findByNome("PACIENTE")
                .orElseThrow(() ->
                        new RuntimeException(
                                "Perfil PACIENTE não encontrado"
                        ));

        Usuario usuario = paciente.getUsuario();

        usuario.setSenha(
                passwordEncoder.encode(
                        usuario.getSenha()
                )
        );

        usuario.getPerfis().add(perfilPaciente);

        pacienteRepository.save(paciente);

        return "redirect:/pacientes";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model) {

        Paciente paciente =
                pacienteRepository.findById(id);

        if (paciente == null) {
            return "redirect:/pacientes";
        }

        model.addAttribute(
                "paciente",
                paciente
        );

        return "paciente/form";
    }

    @PostMapping("/atualizar")
    public String atualizar(
            @Valid Paciente paciente,
            BindingResult result) {

        Paciente pacienteExistente =
                pacienteRepository.findById(
                        paciente.getId()
                );

        if (pacienteExistente == null) {
            return "redirect:/pacientes";
        }

        if (result.hasErrors()) {

            paciente.setUsuario(
                    pacienteExistente.getUsuario()
            );

            return "paciente/form";
        }

        pacienteExistente.setNome(
                paciente.getNome()
        );

        pacienteExistente.setCpf(
                paciente.getCpf()
        );

        pacienteExistente.setEmail(
                paciente.getEmail()
        );

        pacienteExistente.setTelefone(
                paciente.getTelefone()
        );

        pacienteRepository.update(
                pacienteExistente
        );

        return "redirect:/pacientes";
    }

    @GetMapping("/{id}/consultas")
    public String historico(
            @PathVariable Long id,
            Model model) {

        Paciente paciente =
                pacienteRepository.findById(id);

        if (paciente == null) {
            return "redirect:/pacientes";
        }

        model.addAttribute(
                "paciente",
                paciente
        );

        model.addAttribute(
                "agendamentos",
                agendamentoRepository
                        .findByPacienteOrderByIdDesc(paciente)
        );

        model.addAttribute(
                "consultas",
                consultaRepository.findByPaciente(id)
        );

        return "paciente/consultas";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        Paciente paciente =
                pacienteRepository.findById(id);

        if (paciente == null) {
            return "redirect:/pacientes";
        }

        if (!agendamentoRepository
                .findByPacienteOrderByIdDesc(paciente)
                .isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Não é possível excluir este paciente, pois existem agendamentos vinculados a ele."
            );

            return "redirect:/pacientes";
        }

        pacienteRepository.delete(id);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Paciente excluído com sucesso."
        );

        return "redirect:/pacientes";
    }
}