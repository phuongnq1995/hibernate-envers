package org.phuongnq.hibernate_envers.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import org.phuongnq.hibernate_envers.domain.Address;
import org.phuongnq.hibernate_envers.domain.Language;
import org.phuongnq.hibernate_envers.domain.Store;
import org.phuongnq.hibernate_envers.model.StoreDTO;
import org.phuongnq.hibernate_envers.repos.AddressRepository;
import org.phuongnq.hibernate_envers.repos.LanguageRepository;
import org.phuongnq.hibernate_envers.repos.StoreRepository;
import org.phuongnq.hibernate_envers.service.StoreService;
import org.phuongnq.hibernate_envers.util.CustomCollectors;
import org.phuongnq.hibernate_envers.util.ReferencedWarning;
import org.phuongnq.hibernate_envers.util.WebUtils;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;
    private final AddressRepository addressRepository;
    private final StoreRepository storeRepository;
    private final LanguageRepository languageRepository;

    public StoreController(final StoreService storeService,
            final AddressRepository addressRepository, final StoreRepository storeRepository,
            final LanguageRepository languageRepository) {
        this.storeService = storeService;
        this.addressRepository = addressRepository;
        this.storeRepository = storeRepository;
        this.languageRepository = languageRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("masterAddressValues", addressRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Address::getId, Address::getId)));
        model.addAttribute("deliveryAddressValues", addressRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Address::getId, Address::getId)));
        model.addAttribute("languagesValues", languageRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Language::getId, Language::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("stores", storeService.findAll());
        return "store/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("store") final StoreDTO storeDTO) {
        return "store/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("store") @Valid final StoreDTO storeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "store/add";
        }
        storeService.create(storeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("store.create.success"));
        return "redirect:/stores";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id, final Model model) {
        model.addAttribute("store", storeService.get(id));
        return "store/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id,
            @ModelAttribute("store") @Valid final StoreDTO storeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "store/edit";
        }
        storeService.update(id, storeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("store.update.success"));
        return "redirect:/stores";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final UUID id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = storeService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            storeService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("store.delete.success"));
        }
        return "redirect:/stores";
    }

}
