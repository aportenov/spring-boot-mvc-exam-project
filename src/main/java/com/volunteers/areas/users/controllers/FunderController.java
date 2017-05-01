package com.volunteers.areas.users.controllers;

import com.volunteers.areas.users.models.view.FunderViewModel;
import com.volunteers.areas.users.models.view.FunderViewModelBasic;
import com.volunteers.areas.users.models.binding.RegisterFunderModel;
import com.volunteers.errors.Errors;
import com.volunteers.areas.users.services.FunderService;
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
public class FunderController  {


    private final  FunderService funderService;

    @Autowired
    public FunderController(FunderService funderService) {
        this.funderService = funderService;
    }

    @PostMapping("/register/funder")
    public String registerFunder(@Valid @ModelAttribute RegisterFunderModel registerFunderModel,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("funderError", Errors.INVALID_FUNDER);
            redirectAttributes.addFlashAttribute("registerFunderModel", registerFunderModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerFunderModel", bindingResult);

            return "redirect:/register";
        }

        this.funderService.register(registerFunderModel);

        return "redirect:/";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/funders/all")
    public String showFundersPage(Model model, @PageableDefault(size = 20) Pageable pageable){
        Page<FunderViewModelBasic> funderViewModelBasicList = this.funderService.findAll(pageable);
        model.addAttribute("funders", funderViewModelBasicList);
        model.addAttribute("title" , "Funders");

        return "/funders/show-funders";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (#id == principal.id)")
    @GetMapping("/funder/{id}")
    public String showSingleFunderPage(@PathVariable long id, Model model){
        FunderViewModel funderViewModel = null;
        if (! model.containsAttribute("registerFunderModel")) {
            funderViewModel = this.funderService.findOne(id);
            model.addAttribute("registerFunderModel", funderViewModel);
        }

        model.addAttribute("title" , "Edit Funder");

        return "/funders/edit-funder";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (#id == principal.id)")
    @PostMapping("/funder/update/{id}")
    public String updateFunder(@PathVariable long id, @Valid @ModelAttribute RegisterFunderModel registerFunderModel,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes ){

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerFunderModel", bindingResult);
            redirectAttributes.addFlashAttribute("registerFunderModel", registerFunderModel);
            return "redirect:/funder/" + id;
        }

        this.funderService.update(registerFunderModel);

        return "redirect:/funders/all";
    }

}


