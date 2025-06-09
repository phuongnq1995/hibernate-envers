package org.phuongnq.hibernate_envers.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import org.phuongnq.hibernate_envers.model.LanguageDTO;
import org.phuongnq.hibernate_envers.service.LanguageService;
import org.phuongnq.hibernate_envers.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/languages")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(final LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("languages", languageService.findAll());
        return "language/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("language") final LanguageDTO languageDTO) {
        return "language/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("language") @Valid final LanguageDTO languageDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "language/add";
        }
        languageService.create(languageDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("language.create.success"));
        return "redirect:/languages";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id, final Model model) {
        model.addAttribute("language", languageService.get(id));
        return "language/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id,
            @ModelAttribute("language") @Valid final LanguageDTO languageDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "language/edit";
        }
        languageService.update(id, languageDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("language.update.success"));
        return "redirect:/languages";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final UUID id,
            final RedirectAttributes redirectAttributes) {
        languageService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("language.delete.success"));
        return "redirect:/languages";
    }

}
