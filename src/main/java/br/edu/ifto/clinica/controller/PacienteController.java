package br.edu.ifto.clinica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifto.clinica.model.Paciente;
import br.edu.ifto.clinica.repository.ConsultaRepository;
import br.edu.ifto.clinica.repository.PacienteRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pacientes", pacienteRepository.findAll());

        return "paciente/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("paciente", new Paciente());

        return "paciente/form";
    }

    @PostMapping("/salvar")
    public String salvar(
            @Valid Paciente paciente,
            BindingResult result) {

        if (result.hasErrors()) {
            return "paciente/form";
        }

        pacienteRepository.save(paciente);

        return "redirect:/pacientes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("paciente", pacienteRepository.findById(id));

        return "paciente/form";
    }

    @PostMapping("/atualizar")
    public String atualizar(
            @Valid Paciente paciente,
            BindingResult result) {

        if (result.hasErrors()) {
            return "paciente/form";
        }

        pacienteRepository.update(paciente);

        return "redirect:/pacientes";
    }

    @GetMapping("/{id}/consultas")
    public String consultas(@PathVariable Long id, Model model) {

        model.addAttribute(
                "paciente",
                pacienteRepository.findById(id)
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

        if (!consultaRepository.findByPaciente(id).isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Não é possível excluir este paciente, pois existem consultas vinculadas a ele."
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