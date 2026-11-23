package br.edu.ifto.clinica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifto.clinica.model.Medico;
import br.edu.ifto.clinica.model.Perfil;
import br.edu.ifto.clinica.model.Usuario;
import br.edu.ifto.clinica.repository.ConsultaRepository;
import br.edu.ifto.clinica.repository.EspecialidadeRepository;
import br.edu.ifto.clinica.repository.MedicoRepository;
import br.edu.ifto.clinica.repository.PerfilRepository;
import br.edu.ifto.clinica.repository.UsuarioRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "medicos",
                medicoRepository.findAll()
        );

        return "medico/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {

        Medico medico = new Medico();
        medico.setUsuario(new Usuario());

        model.addAttribute(
                "medico",
                medico
        );

        model.addAttribute(
                "especialidades",
                especialidadeRepository.findAll()
        );

        return "medico/form";
    }

    @PostMapping("/salvar")
    public String salvar(
            @Valid Medico medico,
            BindingResult result,
            Model model) {

        if (medico.getUsuario() != null
                && usuarioRepository
                    .findByUsuario(
                            medico.getUsuario().getUsuario()
                    )
                    .isPresent()) {

            result.rejectValue(
                    "usuario.usuario",
                    "usuario.duplicado",
                    "Este nome de usuário já está cadastrado"
            );
        }

        if (result.hasErrors()) {

            model.addAttribute(
                    "especialidades",
                    especialidadeRepository.findAll()
            );

            return "medico/form";
        }

        Perfil perfilMedico = perfilRepository
                .findByNome("MEDICO")
                .orElseThrow(() ->
                        new RuntimeException(
                                "Perfil MEDICO não encontrado"
                        ));

        Usuario usuario = medico.getUsuario();

        usuario.setSenha(
                passwordEncoder.encode(
                        usuario.getSenha()
                )
        );

        usuario.getPerfis().add(
                perfilMedico
        );

        medicoRepository.save(medico);

        return "redirect:/medicos";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "medico",
                medicoRepository.findById(id)
        );

        model.addAttribute(
                "especialidades",
                especialidadeRepository.findAll()
        );

        return "medico/form";
    }

    @PostMapping("/atualizar")
    public String atualizar(
            @Valid Medico medico,
            BindingResult result,
            Model model) {

        Medico medicoExistente =
                medicoRepository.findById(
                        medico.getId()
                );

        if (medicoExistente == null) {
            return "redirect:/medicos";
        }

        if (result.hasErrors()) {

            medico.setUsuario(
                    medicoExistente.getUsuario()
            );

            model.addAttribute(
                    "especialidades",
                    especialidadeRepository.findAll()
            );

            return "medico/form";
        }

        medicoExistente.setNome(
                medico.getNome()
        );

        medicoExistente.setCpf(
                medico.getCpf()
        );

        medicoExistente.setEmail(
                medico.getEmail()
        );

        medicoExistente.setTelefone(
                medico.getTelefone()
        );

        medicoExistente.setCrm(
                medico.getCrm()
        );

        medicoExistente.setEspecialidade(
                medico.getEspecialidade()
        );

        medicoRepository.update(
                medicoExistente
        );

        return "redirect:/medicos";
    }

    @GetMapping("/{id}/consultas")
    public String consultas(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "medico",
                medicoRepository.findById(id)
        );

        model.addAttribute(
                "consultas",
                consultaRepository.findByMedico(id)
        );

        return "medico/consultas";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        if (!consultaRepository
                .findByMedico(id)
                .isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Não é possível excluir este médico, pois existem consultas vinculadas a ele."
            );

            return "redirect:/medicos";
        }

        medicoRepository.delete(id);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Médico excluído com sucesso."
        );

        return "redirect:/medicos";
    }
}