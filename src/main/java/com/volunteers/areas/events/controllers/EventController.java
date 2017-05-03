package com.volunteers.areas.events.controllers;

import com.volunteers.areas.events.models.binding.EditEventModel;
import com.volunteers.areas.events.models.binding.FunderAndEventModel;
import com.volunteers.areas.events.models.binding.RegisterEventModel;
import com.volunteers.areas.events.models.binding.VolunteerAndEventModel;
import com.volunteers.areas.events.models.view.EventMyViewModel;
import com.volunteers.areas.events.models.view.EventViewModel;
import com.volunteers.areas.events.models.view.EventViewModelFull;
import com.volunteers.areas.events.services.EventService;
import com.volunteers.areas.users.entities.Volunteer;
import com.volunteers.config.Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events/all")
    public String showEventsPage(@RequestParam(value = "searchWord", required = false) String searchWord,
                                 Model model,
                                 @PageableDefault(size = 9) Pageable pageable){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasUserRoles = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_USER")
                        || r.getAuthority().equals("ROLE_SOCIAL"));


        Page<EventViewModel> eventViewModels = null;
        searchWord = searchWord == null ? "" : searchWord;
        if (hasUserRoles) {
            Long id = ((Volunteer)authentication.getPrincipal()).getId();
            eventViewModels = this.eventService.findWhereCurrentVolunteerIsNull(id,pageable,searchWord);
        }else{
           eventViewModels = this.eventService.findAll(pageable,searchWord);

        }

        model.addAttribute("events", eventViewModels);
        model.addAttribute("title" , "Events");
        model.addAttribute("key", Params.GOOGLE_KEY);

        return "/events/show-events";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER, ROLE_SOCIAL')")
    @PutMapping("/events/participate")
    public ResponseEntity addVolunteer(@RequestBody VolunteerAndEventModel volunteerAndEventModel) {
        long volunteerId = volunteerAndEventModel.getVolunteerId();
        long eventId = volunteerAndEventModel.getEventId();
        this.eventService.addSingleVolunteer(volunteerId,eventId);


      return  new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (#id == principal.id)")
    @GetMapping("/events/organization/{id}")
    public String getEventsByOrganizationIdPage(@PathVariable long id,
                                                Model model,
                                                @PageableDefault(size = 20) Pageable pageable){
        Page<EventMyViewModel> eventViewModelBasics = this.eventService.findAllByOrganization(id,pageable);
        model.addAttribute("events", eventViewModelBasics);
        model.addAttribute("title" , "My Events");

        return "/myevents";

    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (#id == principal.id)")
    @GetMapping("/events/funder/{id}")
    public String getEventsByFunderIdPage(@PathVariable long id,
                                          Model model,
                                          @PageableDefault(size = 20) Pageable pageable){
        Page<EventMyViewModel> eventViewModelBasics = this.eventService.findAllByFunder(id, pageable);
        model.addAttribute("events", eventViewModelBasics);
        model.addAttribute("title" , "Funded Events");

        return "/myevents";

    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.id")
    @GetMapping("/events/volunteer/{id}")
    public String getEventsByVolunteerIdPage(@PathVariable long id,
                                             Model model,
                                             @PageableDefault(size = 20) Pageable pageable){
        Page<EventMyViewModel> eventViewModelBasics = this.eventService.findAllByVolunteer(id, pageable);
        model.addAttribute("events", eventViewModelBasics);
        model.addAttribute("title" , "My Events");

        return "/myevents";

    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or (isMember(#eventId))")
    @GetMapping("/events/edit/{eventId}")
    public String showEditEventPage(@PathVariable long eventId, Model model){
        if (! model.containsAttribute("event")) {
            EventViewModelFull eventViewModel = this.eventService.findById(eventId);
            model.addAttribute("event", eventViewModel);
        }

        model.addAttribute("title" , "My Events");

        return "/events/edit";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (isMember(#eventId))")
    @GetMapping("/events/delete/{eventId}")
    public String showDeleteEventPage(@PathVariable long eventId, Model model){
        if (! model.containsAttribute("event")) {
            EventViewModelFull eventViewModel = this.eventService.findById(eventId);
            model.addAttribute("event", eventViewModel);
        }

        model.addAttribute("title" , "Delete Event");

        return "/events/delete";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (isMember(#eventId))")
    @PostMapping("/events/delete/{eventId}")
    public String deleteEvent(@PathVariable long eventId, Model model){
        this.eventService.deleteEvent(eventId);
        return "/events/delete";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_ORGANIZATION')")
    @GetMapping("/events/add")
    public String showAddEventPage(Model model){
        if (! model.containsAttribute("event")) {
            model.addAttribute("event", new RegisterEventModel());
        }

        model.addAttribute("title" , "Create Event");

        return "/events/add";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_ORGANIZATION')")
    @PostMapping("/events/add")
    public String addEvent(@Valid @ModelAttribute RegisterEventModel registerEventModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes ){

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.event", bindingResult);
            redirectAttributes.addFlashAttribute("event", registerEventModel);

            return "redirect:/events/add";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String organizationName = auth.getName();
        this.eventService.create(registerEventModel, organizationName);

        return "redirect:/events/all";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_FUNDER')")
    @PutMapping("/events/fundit")
    public ResponseEntity addFunder(@RequestBody FunderAndEventModel funderAndEventModel) {
        long funderId = funderAndEventModel.getFunderId();
        long eventId = funderAndEventModel.getEventId();
        this.eventService.addSingleFunder(funderId,eventId);


        return  new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_ORGANIZATION')")
    @PostMapping("/event/update/{id}")
    public String updateFunder(@PathVariable long id, @Valid @ModelAttribute EditEventModel editEventModel,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes ){

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.event", bindingResult);
            redirectAttributes.addFlashAttribute("event", editEventModel);
            return "redirect:/events/edit/" + id;
        }

        this.eventService.update(editEventModel);

        return "redirect:/events/all";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/events/admin")
    public String getEvents(Model model,
                            @PageableDefault(size = 20) Pageable pageable){
        Page<EventMyViewModel> eventViewModelBasics = this.eventService.findAllAdminView(pageable);
        model.addAttribute("events", eventViewModelBasics);
        model.addAttribute("title" , "Admin Panel - Events");

        return "/myevents";

    }
}
