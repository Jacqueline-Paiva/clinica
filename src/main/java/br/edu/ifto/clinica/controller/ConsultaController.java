package br.edu.ifto.clinica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifto.clinica.model.Consulta;
import br.edu.ifto.clinica.model.Medico;
import br.edu.ifto.clinica.model.Paciente;
import br.edu.ifto.clinica.repository.ConsultaRepository;
import br.edu.ifto.clinica.repository.MedicoRepository;
import br.edu.ifto.clinica.repository.PacienteRepository;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;


    /*
     * LISTAGEM
     *
     * Médico visualiza apenas suas consultas.
     * Paciente visualiza apenas suas consultas.
     * Secretaria e administrador visualizam todas.
     */

    @GetMapping
    public String listar(
            Model model,
            Authentication authentication) {

        if (temPerfil(authentication, "ROLE_MEDICO")) {

            Medico medico =
                    medicoRepository.findByUsuario(
                            authentication.getName()
                    );

            if (medico != null) {

                model.addAttribute(
                        "consultas",
                        consultaRepository
                                .findByMedico(medico.getId())
                );

            } else {

                model.addAttribute(
                        "consultas",
                        List.of()
                );
            }

        } else if (
                temPerfil(
                        authentication,
                        "ROLE_PACIENTE"
                )) {

            Paciente paciente =
                    pacienteRepository.findByUsuario(
                            authentication.getName()
                    );

            if (paciente != null) {

                model.addAttribute(
                        "consultas",
                        consultaRepository
                                .findByPaciente(paciente.getId())
                );

            } else {

                model.addAttribute(
                        "consultas",
                        List.of()
                );
            }

        } else {

            model.addAttribute(
                    "consultas",
                    consultaRepository.findAll()
            );
        }

        return "consulta/list";
    }


    /*
     * ATENDIMENTO
     *
     * A consulta já existe porque foi criada
     * pela confirmação de presença.
     *
     * O médico apenas registra as informações
     * do atendimento.
     */

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Consulta consulta =
                consultaRepository.findById(id);

        if (consulta == null) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Consulta não encontrada."
            );

            return "redirect:/consultas";
        }

        /*
         * Médico só pode editar consulta própria.
         */
        if (temPerfil(authentication, "ROLE_MEDICO")) {

            Medico medico =
                    medicoRepository.findByUsuario(
                            authentication.getName()
                    );

            if (medico == null
                    || !consulta
                            .getMedico()
                            .getId()
                            .equals(medico.getId())) {

                redirectAttributes.addFlashAttribute(
                        "erro",
                        "Você não pode editar esta consulta."
                );

                return "redirect:/consultas";
            }
        }

        model.addAttribute(
                "consulta",
                consulta
        );

        return "consulta/form";
    }


    /*
     * SALVA OS DADOS DO ATENDIMENTO
     *
     * Paciente, médico, data e agendamento
     * não podem ser alterados aqui.
     */

    @PostMapping("/atualizar")
    public String atualizar(
            @RequestParam Long id,
            @RequestParam(required = false) Double valor,
            @RequestParam(required = false) String observacao,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        Consulta consulta =
                consultaRepository.findById(id);

        if (consulta == null) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Consulta não encontrada."
            );

            return "redirect:/consultas";
        }

        /*
         * Garante que o médico só altere
         * suas próprias consultas.
         */
        if (temPerfil(authentication, "ROLE_MEDICO")) {

            Medico medico =
                    medicoRepository.findByUsuario(
                            authentication.getName()
                    );

            if (medico == null
                    || !consulta
                            .getMedico()
                            .getId()
                            .equals(medico.getId())) {

                redirectAttributes.addFlashAttribute(
                        "erro",
                        "Você não pode editar esta consulta."
                );

                return "redirect:/consultas";
            }
        }

        /*
         * Validação do valor.
         */
        if (valor == null || valor <= 0) {

            model.addAttribute(
                    "consulta",
                    consulta
            );

            model.addAttribute(
                    "erroValor",
                    "Informe um valor maior que zero."
            );

            return "consulta/form";
        }

        consulta.setValor(valor);
        consulta.setObservacao(observacao);

        consultaRepository.update(consulta);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Atendimento registrado com sucesso."
        );

        return "redirect:/consultas";
    }


    /*
     * EXCLUSÃO
     *
     * O SecurityConfiguration permite
     * esta operação apenas ao ADMIN.
     */

    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        Consulta consulta =
                consultaRepository.findById(id);

        if (consulta == null) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Consulta não encontrada."
            );

            return "redirect:/consultas";
        }

        consultaRepository.delete(id);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Consulta excluída com sucesso."
        );

        return "redirect:/consultas";
    }


    private boolean temPerfil(
            Authentication authentication,
            String perfil) {

        return authentication
                .getAuthorities()
                .stream()
                .anyMatch(
                        autoridade ->
                                autoridade
                                        .getAuthority()
                                        .equals(perfil)
                );
    }
}