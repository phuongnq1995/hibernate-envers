package org.phuongnq.hibernate_envers.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import org.phuongnq.hibernate_envers.model.AddressDTO;
import org.phuongnq.hibernate_envers.service.AddressService;
import org.phuongnq.hibernate_envers.util.ReferencedWarning;
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
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(final AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("addresses", addressService.findAll());
        return "address/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("address") final AddressDTO addressDTO) {
        return "address/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("address") @Valid final AddressDTO addressDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "address/add";
        }
        addressService.create(addressDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("address.create.success"));
        return "redirect:/addresses";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id, final Model model) {
        model.addAttribute("address", addressService.get(id));
        return "address/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id,
            @ModelAttribute("address") @Valid final AddressDTO addressDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "address/edit";
        }
        addressService.update(id, addressDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("address.update.success"));
        return "redirect:/addresses";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final UUID id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = addressService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            addressService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("address.delete.success"));
        }
        return "redirect:/addresses";
    }

}
