package br.edu.ifto.clinica.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifto.clinica.model.Agenda;
import br.edu.ifto.clinica.model.Agendamento;
import br.edu.ifto.clinica.model.Consulta;
import br.edu.ifto.clinica.model.Medico;
import br.edu.ifto.clinica.model.Paciente;
import br.edu.ifto.clinica.model.StatusAgenda;
import br.edu.ifto.clinica.model.StatusAgendamento;
import br.edu.ifto.clinica.model.TipoAgendamento;
import br.edu.ifto.clinica.repository.AgendaRepository;
import br.edu.ifto.clinica.repository.AgendamentoRepository;
import br.edu.ifto.clinica.repository.ConsultaRepository;
import br.edu.ifto.clinica.repository.MedicoRepository;
import br.edu.ifto.clinica.repository.PacienteRepository;

@Controller
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @GetMapping
    public String listar(
            Model model,
            Authentication authentication) {

        if (temPerfil(authentication, "ROLE_PACIENTE")) {

            Paciente paciente = pacienteRepository
                    .findByUsuario(authentication.getName());

            if (paciente != null) {
                model.addAttribute(
                        "agendamentos",
                        agendamentoRepository
                                .findByPacienteOrderByIdDesc(paciente)
                );
            } else {
                model.addAttribute("agendamentos", List.of());
            }

        } else if (temPerfil(authentication, "ROLE_MEDICO")) {

            Medico medico = medicoRepository
                    .findByUsuario(authentication.getName());

            if (medico != null) {
                model.addAttribute(
                        "agendamentos",
                        agendamentoRepository
                                .findByAgendaMedicoOrderByIdDesc(medico)
                );
            } else {
                model.addAttribute("agendamentos", List.of());
            }

        } else {

            model.addAttribute(
                    "agendamentos",
                    agendamentoRepository.findAllByOrderByIdDesc()
            );
        }

        return "agendamento/list";
    }

    @GetMapping("/novo")
    public String novo(
            Model model,
            Authentication authentication) {

        carregarFormulario(model, authentication);

        return "agendamento/form";
    }

    @PostMapping("/salvar")
    @Transactional
    public String salvar(
            @RequestParam Long agendaId,
            @RequestParam(required = false) Long pacienteId,
            @RequestParam TipoAgendamento tipo,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        Agenda agenda = agendaRepository
                .findById(agendaId)
                .orElse(null);

        Paciente paciente;

        if (temPerfil(authentication, "ROLE_PACIENTE")) {

            paciente = pacienteRepository
                    .findByUsuario(authentication.getName());

        } else {

            paciente = pacienteId == null
                    ? null
                    : pacienteRepository.findById(pacienteId);
        }

        if (agenda == null || paciente == null) {

            model.addAttribute(
                    "erro",
                    "Horário ou paciente inválido."
            );

            carregarFormulario(model, authentication);

            return "agendamento/form";
        }

        if (agenda.getStatus() != StatusAgenda.DISPONIVEL) {

            model.addAttribute(
                    "erro",
                    "Este horário não está mais disponível."
            );

            carregarFormulario(model, authentication);

            return "agendamento/form";
        }

        if (agendamentoRepository.existsByAgendaAndStatus(
                agenda,
                StatusAgendamento.AGENDADO)) {

            model.addAttribute(
                    "erro",
                    "Este horário já possui um agendamento."
            );

            carregarFormulario(model, authentication);

            return "agendamento/form";
        }

        Agendamento agendamento = new Agendamento();

        agendamento.setAgenda(agenda);
        agendamento.setPaciente(paciente);
        agendamento.setTipo(tipo);
        agendamento.setStatus(StatusAgendamento.AGENDADO);

        agendamentoRepository.save(agendamento);

        agenda.setStatus(StatusAgenda.AGENDADO);
        agendaRepository.save(agenda);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Agendamento realizado com sucesso."
        );

        return "redirect:/agendamentos";
    }

    @GetMapping("/cancelar/{id}")
    @Transactional
    public String cancelar(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Agendamento agendamento = agendamentoRepository
                .findById(id)
                .orElse(null);

        if (agendamento == null) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Agendamento não encontrado."
            );

            return "redirect:/agendamentos";
        }

        if (temPerfil(authentication, "ROLE_PACIENTE")) {

            Paciente paciente = pacienteRepository
                    .findByUsuario(authentication.getName());

            if (paciente == null
                    || !agendamento.getPaciente().getId()
                            .equals(paciente.getId())) {

                redirectAttributes.addFlashAttribute(
                        "erro",
                        "Você não pode cancelar este agendamento."
                );

                return "redirect:/agendamentos";
            }
        }

        if (temPerfil(authentication, "ROLE_MEDICO")) {

            Medico medico = medicoRepository
                    .findByUsuario(authentication.getName());

            if (medico == null
                    || !agendamento.getAgenda()
                            .getMedico()
                            .getId()
                            .equals(medico.getId())) {

                redirectAttributes.addFlashAttribute(
                        "erro",
                        "Você não pode cancelar este agendamento."
                );

                return "redirect:/agendamentos";
            }
        }

        if (agendamento.getStatus()
                != StatusAgendamento.AGENDADO) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Este agendamento não pode ser cancelado."
            );

            return "redirect:/agendamentos";
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);

        Agenda agenda = agendamento.getAgenda();
        agenda.setStatus(StatusAgenda.DISPONIVEL);

        agendamentoRepository.save(agendamento);
        agendaRepository.save(agenda);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Agendamento cancelado. O horário voltou a ficar disponível."
        );

        return "redirect:/agendamentos";
    }

    @GetMapping("/confirmar/{id}")
    @Transactional
    public String confirmarPresenca(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        Agendamento agendamento = agendamentoRepository
                .findById(id)
                .orElse(null);

        if (agendamento == null) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Agendamento não encontrado."
            );

            return "redirect:/agendamentos";
        }

        if (agendamento.getStatus()
                != StatusAgendamento.AGENDADO) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Somente agendamentos ativos podem ter presença confirmada."
            );

            return "redirect:/agendamentos";
        }

        if (consultaRepository
                .findByAgendamento(agendamento.getId()) != null) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Já existe uma consulta para este agendamento."
            );

            return "redirect:/agendamentos";
        }

        Agenda agenda = agendamento.getAgenda();

        LocalDateTime dataConsulta = LocalDateTime.of(
                agenda.getData(),
                agenda.getHorario()
        );

        Consulta consulta = new Consulta();

        consulta.setData(dataConsulta);
        consulta.setPaciente(agendamento.getPaciente());
        consulta.setMedico(agenda.getMedico());
        consulta.setAgendamento(agendamento);

        consultaRepository.save(consulta);

        agendamento.setStatus(StatusAgendamento.REALIZADO);
        agendamentoRepository.save(agendamento);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Presença confirmada e consulta criada com sucesso."
        );

        return "redirect:/agendamentos";
    }

    private void carregarFormulario(
            Model model,
            Authentication authentication) {

        model.addAttribute(
                "horarios",
                agendaRepository
                        .findByStatusOrderByDataAscHorarioAsc(
                                StatusAgenda.DISPONIVEL
                        )
        );

        model.addAttribute(
                "tipos",
                TipoAgendamento.values()
        );

        boolean pacienteLogado =
                temPerfil(authentication, "ROLE_PACIENTE");

        model.addAttribute(
                "pacienteLogado",
                pacienteLogado
        );

        if (pacienteLogado) {

            model.addAttribute(
                    "paciente",
                    pacienteRepository
                            .findByUsuario(authentication.getName())
            );

        } else {

            model.addAttribute(
                    "pacientes",
                    pacienteRepository.findAll()
            );
        }
    }

    private boolean temPerfil(
            Authentication authentication,
            String perfil) {

        return authentication.getAuthorities()
                .stream()
                .anyMatch(
                        autoridade ->
                                autoridade.getAuthority().equals(perfil)
                );
    }
}