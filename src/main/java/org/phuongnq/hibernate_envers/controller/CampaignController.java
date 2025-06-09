package org.phuongnq.hibernate_envers.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import org.phuongnq.hibernate_envers.domain.Store;
import org.phuongnq.hibernate_envers.model.CampaignDTO;
import org.phuongnq.hibernate_envers.repos.StoreRepository;
import org.phuongnq.hibernate_envers.service.CampaignService;
import org.phuongnq.hibernate_envers.util.CustomCollectors;
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
@RequestMapping("/campaigns")
public class CampaignController {

    private final CampaignService campaignService;
    private final StoreRepository storeRepository;

    public CampaignController(final CampaignService campaignService,
            final StoreRepository storeRepository) {
        this.campaignService = campaignService;
        this.storeRepository = storeRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("storeValues", storeRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Store::getId, Store::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("campaigns", campaignService.findAll());
        return "campaign/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("campaign") final CampaignDTO campaignDTO) {
        return "campaign/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("campaign") @Valid final CampaignDTO campaignDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "campaign/add";
        }
        campaignService.create(campaignDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("campaign.create.success"));
        return "redirect:/campaigns";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id, final Model model) {
        model.addAttribute("campaign", campaignService.get(id));
        return "campaign/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id,
            @ModelAttribute("campaign") @Valid final CampaignDTO campaignDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "campaign/edit";
        }
        campaignService.update(id, campaignDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("campaign.update.success"));
        return "redirect:/campaigns";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final UUID id,
            final RedirectAttributes redirectAttributes) {
        campaignService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("campaign.delete.success"));
        return "redirect:/campaigns";
    }

}
