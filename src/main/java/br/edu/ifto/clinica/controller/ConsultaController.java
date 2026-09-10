package br.edu.ifto.clinica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifto.clinica.model.Consulta;
import br.edu.ifto.clinica.repository.ConsultaRepository;
import br.edu.ifto.clinica.repository.MedicoRepository;
import br.edu.ifto.clinica.repository.PacienteRepository;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("consultas", consultaRepository.findAll());

        return "consulta/list";
    }

    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("consulta", new Consulta());
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("medicos", medicoRepository.findAll());

        return "consulta/form";
    }

    @PostMapping("/salvar")
    public String salvar(
            Consulta consulta,
            @RequestParam Long pacienteId,
            @RequestParam Long medicoId) {

        consulta.setPaciente(pacienteRepository.findById(pacienteId));
        consulta.setMedico(medicoRepository.findById(medicoId));

        consultaRepository.save(consulta);

        return "redirect:/consultas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("consulta", consultaRepository.findById(id));
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("medicos", medicoRepository.findAll());

        return "consulta/form";
    }

    @PostMapping("/atualizar")
    public String atualizar(
            Consulta consulta,
            @RequestParam Long pacienteId,
            @RequestParam Long medicoId) {

        consulta.setPaciente(pacienteRepository.findById(pacienteId));
        consulta.setMedico(medicoRepository.findById(medicoId));

        consultaRepository.update(consulta);

        return "redirect:/consultas";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        consultaRepository.delete(id);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Consulta excluída com sucesso."
        );

        return "redirect:/consultas";
    }
}