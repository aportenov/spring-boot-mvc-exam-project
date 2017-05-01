package com.volunteers.areas.users.controllers;

import com.volunteers.areas.users.models.binding.RegisterVolunteerModel;
import com.volunteers.areas.users.models.view.VolunteerViewModelBasic;
import com.volunteers.areas.users.services.VolunteerService;
import com.volunteers.errors.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class VolunteerController {


    private final VolunteerService volunteerService;

    @Autowired
    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }


    @PostMapping("/register/volunteer")
    public String registerVolunteer(@Valid @ModelAttribute RegisterVolunteerModel registerVolunteerModel,
                                    BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("volunteerError", Errors.INVALID_VOLUTEER);
            redirectAttributes.addFlashAttribute("registerVolunteerModel", registerVolunteerModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerVolunteerModel", bindingResult);

            return "redirect:/register";
        }

        this.volunteerService.register(registerVolunteerModel);

        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ORGANIZATION')")
    @GetMapping("/volunteers/event/{eventId}")
    public ResponseEntity<List<VolunteerViewModelBasic>> getVolunteersPerEvent(@PathVariable Long eventId){
            List<VolunteerViewModelBasic> volunteers = this.volunteerService.findAllByEventId(eventId);

        if(volunteers == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        ResponseEntity<List<VolunteerViewModelBasic>> responseEntity
                = new ResponseEntity(volunteers, HttpStatus.OK);

        return responseEntity;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/volunteers/all")
    public String getShowVolunteersPage(Model model, @PageableDefault(size = 20) Pageable pageable){
        Page<VolunteerViewModelBasic> volunteerViewModelBasics = this.volunteerService.findAll(pageable);
        model.addAttribute("volunteers", volunteerViewModelBasics);
        model.addAttribute("title" , "Volunteers");

        return "/volunteers/show-volunteers";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.id")
    @GetMapping("/volunteer/{id}")
    public String showSignleVolunteerPage(@PathVariable long id, Model model){
        RegisterVolunteerModel registerVolunteerModel = null;
        if (! model.containsAttribute("registerVolunteerModel")) {
            registerVolunteerModel = this.volunteerService.findOne(id);
            model.addAttribute("registerVolunteerModel", registerVolunteerModel);
        }

        model.addAttribute("title" , "Edit Volunteer");

        return "/volunteers/edit-volunteer";
    }

    @PreAuthorize("hasRole('ADMIN') or (#id == principal.id)")
    @PostMapping("/volunteer/update/{id}")
    public String updateVolunteer(@PathVariable long id, @Valid @ModelAttribute RegisterVolunteerModel registerVolunteerModel,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes ){

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerVolunteerModel", bindingResult);
            redirectAttributes.addFlashAttribute("registerVolunteerModel", registerVolunteerModel);
            return "redirect:/volunteer/" + id;
        }

        this.volunteerService.update(registerVolunteerModel);

        return "redirect:/volunteers/all";
    }

}
