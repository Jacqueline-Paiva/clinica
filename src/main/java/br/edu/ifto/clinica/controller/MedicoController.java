package br.edu.ifto.clinica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifto.clinica.model.Medico;
import br.edu.ifto.clinica.repository.ConsultaRepository;
import br.edu.ifto.clinica.repository.MedicoRepository;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("medicos", medicoRepository.findAll());

        return "medico/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("medico", new Medico());

        return "medico/form";
    }

    @PostMapping("/salvar")
    public String salvar(Medico medico) {
        medicoRepository.save(medico);

        return "redirect:/medicos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("medico", medicoRepository.findById(id));

        return "medico/form";
    }

    @PostMapping("/atualizar")
    public String atualizar(Medico medico) {
        medicoRepository.update(medico);

        return "redirect:/medicos";
    }

    @GetMapping("/{id}/consultas")
    public String consultas(@PathVariable Long id, Model model) {

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

        if (!consultaRepository.findByMedico(id).isEmpty()) {

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