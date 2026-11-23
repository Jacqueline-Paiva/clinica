package br.edu.ifto.clinica.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifto.clinica.model.Agenda;
import br.edu.ifto.clinica.model.Medico;
import br.edu.ifto.clinica.model.StatusAgenda;
import br.edu.ifto.clinica.repository.AgendaRepository;
import br.edu.ifto.clinica.repository.MedicoRepository;

@Controller
@RequestMapping("/agenda")
public class AgendaController {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping
    public String listar(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) Integer dia,
            Model model,
            Authentication authentication) {

        LocalDate hoje = LocalDate.now();

        int mesSelecionado =
                mes != null ? mes : hoje.getMonthValue();

        int anoSelecionado =
                ano != null ? ano : hoje.getYear();

        YearMonth anoMes = YearMonth.of(
                anoSelecionado,
                mesSelecionado
        );

        int diaSelecionado;

        if (dia != null
                && dia >= 1
                && dia <= anoMes.lengthOfMonth()) {

            diaSelecionado = dia;

        } else if (anoMes.equals(YearMonth.from(hoje))) {

            diaSelecionado = hoje.getDayOfMonth();

        } else {

            diaSelecionado = 1;
        }

        LocalDate dataSelecionada =
                anoMes.atDay(diaSelecionado);

        boolean medico =
                temPerfil(authentication, "ROLE_MEDICO");

        List<Agenda> horarios;

        if (medico) {

            Medico medicoLogado =
                    medicoRepository.findByUsuario(
                            authentication.getName()
                    );

            if (medicoLogado != null) {

                horarios = agendaRepository
                        .findByMedicoOrderByDataAscHorarioAsc(
                                medicoLogado
                        );

            } else {

                horarios = new ArrayList<>();
            }

        } else {

            horarios = new ArrayList<>(
                    agendaRepository.findAll()
            );

            horarios.sort(
                    Comparator
                            .comparing(Agenda::getData)
                            .thenComparing(Agenda::getHorario)
            );
        }

        List<Agenda> horariosDoMes =
                horarios.stream()
                        .filter(h ->
                                YearMonth
                                        .from(h.getData())
                                        .equals(anoMes))
                        .toList();

        List<Agenda> horariosDoDia =
                horariosDoMes.stream()
                        .filter(h ->
                                h.getData()
                                        .equals(dataSelecionada))
                        .sorted(
                                Comparator
                                        .comparing(Agenda::getHorario)
                                        .thenComparing(
                                                h -> h.getMedico().getNome()
                                        )
                        )
                        .toList();

        Map<Integer, Integer> quantidadePorDia =
                new HashMap<>();

        for (Agenda horario : horariosDoMes) {

            int numeroDia =
                    horario.getData().getDayOfMonth();

            quantidadePorDia.put(
                    numeroDia,
                    quantidadePorDia
                            .getOrDefault(numeroDia, 0) + 1
            );
        }

        List<Integer> diasCalendario =
                new ArrayList<>();

        int deslocamento =
                deslocamentoPrimeiroDia(anoMes);

        for (int i = 0; i < deslocamento; i++) {
            diasCalendario.add(null);
        }

        for (int i = 1;
             i <= anoMes.lengthOfMonth();
             i++) {

            diasCalendario.add(i);
        }

        while (diasCalendario.size() % 7 != 0) {
            diasCalendario.add(null);
        }

        YearMonth mesAnterior =
                anoMes.minusMonths(1);

        YearMonth proximoMes =
                anoMes.plusMonths(1);

        String nomeMes =
                anoMes.getMonth()
                        .getDisplayName(
                                TextStyle.FULL,
                                new Locale("pt", "BR")
                        );

        nomeMes =
                nomeMes.substring(0, 1).toUpperCase()
                        + nomeMes.substring(1);

        model.addAttribute(
                "diasCalendario",
                diasCalendario
        );

        model.addAttribute(
                "quantidadePorDia",
                quantidadePorDia
        );

        model.addAttribute(
                "horariosDoDia",
                horariosDoDia
        );

        model.addAttribute("mes", mesSelecionado);
        model.addAttribute("ano", anoSelecionado);
        model.addAttribute("diaSelecionado", diaSelecionado);
        model.addAttribute("dataSelecionada", dataSelecionada);
        model.addAttribute("nomeMes", nomeMes);

        model.addAttribute(
                "mesAnterior",
                mesAnterior.getMonthValue()
        );

        model.addAttribute(
                "anoMesAnterior",
                mesAnterior.getYear()
        );

        model.addAttribute(
                "proximoMes",
                proximoMes.getMonthValue()
        );

        model.addAttribute(
                "anoProximoMes",
                proximoMes.getYear()
        );

        model.addAttribute("hoje", hoje);

        return "agenda/list";
    }

    @GetMapping("/novo")
    public String novo(
            Model model,
            Authentication authentication) {

        boolean medicoLogado =
                temPerfil(authentication, "ROLE_MEDICO");

        if (medicoLogado) {

            Medico medico =
                    medicoRepository.findByUsuario(
                            authentication.getName()
                    );

            model.addAttribute(
                    "medico",
                    medico
            );

        } else {

            model.addAttribute(
                    "medicos",
                    medicoRepository.findAll()
            );
        }

        model.addAttribute(
                "medicoLogado",
                medicoLogado
        );

        return "agenda/form";
    }

    @PostMapping("/salvar")
    public String salvar(
            @RequestParam(required = false) Long medicoId,
            @RequestParam(required = false) LocalDate data,
            @RequestParam(required = false) LocalTime horario,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        boolean medicoLogado =
                temPerfil(authentication, "ROLE_MEDICO");

        Medico medico;

        /*
         * Se o usuário for médico, o profissional é
         * obrigatoriamente obtido pelo login.
         *
         * Assim, um médico não consegue disponibilizar
         * horário em nome de outro profissional.
         */
        if (medicoLogado) {

            medico = medicoRepository.findByUsuario(
                    authentication.getName()
            );

        } else {

            medico = medicoId == null
                    ? null
                    : medicoRepository.findById(medicoId);
        }

        boolean erro = false;

        if (medico == null) {

            model.addAttribute(
                    "erroMedico",
                    "Selecione um médico."
            );

            erro = true;
        }

        if (data == null) {

            model.addAttribute(
                    "erroData",
                    "Informe a data."
            );

            erro = true;

        } else if (data.isBefore(LocalDate.now())) {

            model.addAttribute(
                    "erroData",
                    "A data não pode estar no passado."
            );

            erro = true;
        }

        if (horario == null) {

            model.addAttribute(
                    "erroHorario",
                    "Informe o horário."
            );

            erro = true;
        }

        /*
         * Impede dois horários iguais para o mesmo médico.
         */
        if (!erro
                && agendaRepository
                        .existsByMedicoAndDataAndHorario(
                                medico,
                                data,
                                horario
                        )) {

            model.addAttribute(
                    "erro",
                    "Este médico já possui esse horário cadastrado."
            );

            erro = true;
        }

        if (erro) {

            model.addAttribute(
                    "medicoLogado",
                    medicoLogado
            );

            model.addAttribute(
                    "medico",
                    medico
            );

            model.addAttribute(
                    "data",
                    data
            );

            model.addAttribute(
                    "horario",
                    horario
            );

            if (!medicoLogado) {

                model.addAttribute(
                        "medicos",
                        medicoRepository.findAll()
                );
            }

            return "agenda/form";
        }

        Agenda agenda = new Agenda();

        agenda.setMedico(medico);
        agenda.setData(data);
        agenda.setHorario(horario);
        agenda.setStatus(StatusAgenda.DISPONIVEL);

        agendaRepository.save(agenda);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Horário disponibilizado com sucesso."
        );

        return "redirect:/agenda?mes="
                + data.getMonthValue()
                + "&ano="
                + data.getYear()
                + "&dia="
                + data.getDayOfMonth();
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

    private int deslocamentoPrimeiroDia(
            YearMonth anoMes) {

        DayOfWeek primeiroDia =
                anoMes.atDay(1).getDayOfWeek();

        return primeiroDia.getValue() - 1;
    }
}