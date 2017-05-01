package com.volunteers.areas.users.controllers;

import com.volunteers.areas.users.models.binding.RegisterFunderModel;
import com.volunteers.areas.users.models.binding.RegisterOrganizationModel;
import com.volunteers.areas.users.models.binding.RegisterVolunteerModel;
import com.volunteers.areas.users.models.binding.RegistrationModel;
import com.volunteers.areas.users.services.UserService;
import com.volunteers.errors.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserController {


    private final  UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute RegistrationModel registrationModel,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("registrationModel", registrationModel);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registrationModel", bindingResult);

            return "redirect:/";
        }

        return "redirect:/register";
    }


    @GetMapping("/login")
    public String getLoginPage(@RequestParam(required = false) String error, RedirectAttributes redirectAttributes){
        if(error != null){
            redirectAttributes.addFlashAttribute("error", Errors.INVALID_CREDENTIALS);
        }

        return "redirect:/";
    }


    @GetMapping("/register")
    public String completeRegistration(Model model) {

        if (! model.containsAttribute("registrationModel")) {
            model.addAttribute("registrationModel", new RegistrationModel());
        }

        if (! model.containsAttribute("registerVolunteerModel")) {
            model.addAttribute("registerVolunteerModel", new RegisterVolunteerModel());
        }

        if (! model.containsAttribute("registerOrganizationModel")) {
            model.addAttribute("registerOrganizationModel", new RegisterOrganizationModel());
        }

        if (!model.containsAttribute("registerFunderModel")) {
            model.addAttribute("registerFunderModel", new RegisterFunderModel());
        }

        model.addAttribute("title", "Register");

        return "/register";
    }

   /* @GetMapping("/user")
    public ResponseEntity getLoggedUserAuthority(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean hasUserRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_FUNDER"));

        ResponseEntity<Boolean> responseEntity
                = new ResponseEntity(hasUserRole, HttpStatus.OK);

        return responseEntity;
    }*/

}
