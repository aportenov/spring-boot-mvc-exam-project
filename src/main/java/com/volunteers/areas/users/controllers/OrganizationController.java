package com.volunteers.areas.users.controllers;

import com.volunteers.areas.users.models.view.OrganizationViewModel;
import com.volunteers.areas.users.models.binding.RegisterOrganizationModel;
import com.volunteers.errors.Errors;
import com.volunteers.areas.users.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class OrganizationController {


    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/register/organization")
    public String registerOrganization(@Valid @ModelAttribute RegisterOrganizationModel registerOrganizationModel,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("organizationError", Errors.INVALID_ORGANIZATION);
            redirectAttributes.addFlashAttribute("registerOrganizationModel", registerOrganizationModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerOrganizationModel", bindingResult);

            return "redirect:/register";
        }

        this.organizationService.register(registerOrganizationModel);

        return "redirect:/";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/organizations/all")
    public String showOrganizationsPage(Model model,@PageableDefault(size = 20) Pageable pageable){
        Page<OrganizationViewModel> organizizationViewModels = this.organizationService.findAll(pageable);
        model.addAttribute("organizations", organizizationViewModels);
        model.addAttribute("title" ,"Organizations");

        return "/organizations/show-organizations";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (#id == principal.id)")
    @GetMapping("/organization/{id}")
    public String showSignleOrganizationPage(@PathVariable long id, Model model){
        RegisterOrganizationModel registerOrganizationModel = null;
        if (! model.containsAttribute("registerOrganizationModel")) {
            registerOrganizationModel = this.organizationService.findOne(id);
            model.addAttribute("registerOrganizationModel", registerOrganizationModel);
        }

        model.addAttribute("title" , "Edit Organization");

        return "/organizations/edit-organization";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (#id == principal.id)")
    @PostMapping("/organization/update/{id}")
    public String updateFunder(@PathVariable long id, @Valid @ModelAttribute RegisterOrganizationModel registerOrganizationModel,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes ){

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerOrganizationModel", bindingResult);
            redirectAttributes.addFlashAttribute("registerOrganizationModel", registerOrganizationModel);
            return "redirect:/organization/" + id;
        }

        this.organizationService.update(registerOrganizationModel);

        return "redirect:/organizations/all";
    }
}
