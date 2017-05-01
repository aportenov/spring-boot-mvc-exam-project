package com.volunteers.areas.events.serviceImpl;

import com.volunteers.areas.countries.entities.Country;
import com.volunteers.areas.countries.repositories.CountryRepository;
import com.volunteers.areas.events.entities.Event;
import com.volunteers.areas.events.models.binding.EditEventModel;
import com.volunteers.areas.events.models.binding.RegisterEventModel;
import com.volunteers.areas.events.models.view.EventMyViewModel;
import com.volunteers.areas.events.models.view.EventViewModel;
import com.volunteers.areas.events.models.view.EventViewModelFull;
import com.volunteers.areas.events.repositories.EventRepository;
import com.volunteers.areas.events.services.EventService;
import com.volunteers.areas.users.entities.Funder;
import com.volunteers.areas.users.entities.Organization;
import com.volunteers.areas.users.entities.Volunteer;
import com.volunteers.areas.users.repositories.FunderRepository;
import com.volunteers.areas.users.repositories.OrganizationRepository;
import com.volunteers.areas.users.repositories.VolunteerRepository;
import com.volunteers.enumerators.Status;
import com.volunteers.errors.Errors;
import com.volunteers.errors.EventNotFoundExeption;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private static final int VALUE_ZERO = 0;
    private static final int DEFAILT_VALUE = 1;
    private static final int MY_EVENTS_VALUE = 20;
    private static int EVENTS_VALUE = 20;

    private final CountryRepository countryRepository;
    private final FunderRepository funderRepository;
    private final EventRepository eventRepository;
    private final OrganizationRepository organizationRepository;
    private final ModelMapper modelMapper;
    private final VolunteerRepository volunteerRepository;

    public EventServiceImpl(CountryRepository countryRepository,
                            FunderRepository funderRepository,
                            EventRepository eventRepository,
                            OrganizationRepository organizationRepository,
                            ModelMapper modelMapper,
                            VolunteerRepository volunteerRepository) {
        this.countryRepository = countryRepository;
        this.funderRepository = funderRepository;
        this.eventRepository = eventRepository;
        this.organizationRepository = organizationRepository;
        this.modelMapper = modelMapper;
        this.volunteerRepository = volunteerRepository;
    }

    @Override
    public Page<EventViewModel> findAll(Pageable pageable, String searchWord) {
        List<Event> events = this.eventRepository.findByIsActiveTrue(searchWord);
        List<EventViewModel> eventViewModels = new ArrayList<>();
        for (Event event : events) {
            EventViewModel eventViewModel = this.modelMapper.map(event, EventViewModel.class);

            eventViewModels.add(eventViewModel);
        }

        int totalPages = eventViewModels.size() == VALUE_ZERO ? DEFAILT_VALUE : eventViewModels.size() / EVENTS_VALUE;

        return new PageImpl<>(eventViewModels, pageable, totalPages);
    }

    @Override
    @Transactional
    public void addSingleVolunteer(long volunteerId, long eventId) {
        Event event = this.eventRepository.findOne(eventId);
        Volunteer volunteer = this.volunteerRepository.findOne(volunteerId);
        event.addVolunteer(volunteer);

        this.eventRepository.save(event);

    }

    @Override
    @Transactional
    public Page<EventMyViewModel> findAllByOrganization(long organizationId, Pageable pageable) {
        List<Event> events = this.eventRepository.findByOrganizationId(organizationId);
        List<EventMyViewModel> eventMyViewModels = new ArrayList<>();
        for (Event event : events) {
            EventMyViewModel eventMyViewModel = this.modelMapper.map(event, EventMyViewModel.class);
            eventMyViewModels.add(eventMyViewModel);
        }

        int totalPages = eventMyViewModels.size() == VALUE_ZERO ? DEFAILT_VALUE : eventMyViewModels.size()/ MY_EVENTS_VALUE;

        return new PageImpl<>(eventMyViewModels, pageable, totalPages);
    }

    @Override
    public EventViewModelFull findById(long eventId) {
        Event event = this.eventRepository.findOne(eventId);
        EventViewModelFull eventViewModel = null;
        if (event == null) {
            throw new EventNotFoundExeption(Errors.EVENT_NOT_FOUND);
        }

        eventViewModel = this.modelMapper.map(event, EventViewModelFull.class);
        eventViewModel.setNumberOfVolunteers(event.getVolunteers().size());

        return eventViewModel;
    }

    @Override
    @Transactional
    public void create(RegisterEventModel registerEventModel, String organizationName) {
        Event event = this.modelMapper.map(registerEventModel, Event.class);
        Country country = this.countryRepository.findOneByCountryName(registerEventModel.getCountryName());
        Organization organization = this.organizationRepository.findOneByUsername(organizationName);
        event.setOrganization(organization);
        event.setCountry(country);
        event.setActive(true);

        this.eventRepository.save(event);
    }

    @Override
    @Transactional
    public void addSingleFunder(long funderId, long eventId) {
        Event event = this.eventRepository.findOne(eventId);
        Funder funder = this.funderRepository.findOne(funderId);
        event.setFunder(funder);

        this.eventRepository.save(event);

    }

    @Override
    public Page<EventMyViewModel> findAllByVolunteer(long id, Pageable pageable) {
        List<Event> events = this.eventRepository.findByVolunteer(id);
        List<EventMyViewModel> eventMyViewModels = new ArrayList<>();
        for (Event event : events) {
            EventMyViewModel eventMyViewModel = this.modelMapper.map(event, EventMyViewModel.class);
            eventMyViewModels.add(eventMyViewModel);
        }

        int totalPages = eventMyViewModels.size() == VALUE_ZERO ? DEFAILT_VALUE : eventMyViewModels.size() / EVENTS_VALUE;

        return new PageImpl<>(eventMyViewModels, pageable, totalPages);
    }

    @Override
    public Page<EventMyViewModel> findAllByFunder(long id, Pageable pageable) {
        List<Event> events = this.eventRepository.findByFunder(id);
        List<EventMyViewModel> eventMyViewModels = new ArrayList<>();
        for (Event event : events) {
            EventMyViewModel eventMyViewModel = this.modelMapper.map(event, EventMyViewModel.class);
            eventMyViewModels.add(eventMyViewModel);
        }

        int totalPages = eventMyViewModels.size() == VALUE_ZERO ? DEFAILT_VALUE : eventMyViewModels.size() / MY_EVENTS_VALUE;

        return new PageImpl<>(eventMyViewModels, pageable, totalPages);
    }

    @Override
    public Page<EventViewModel> findWhereCurrentVolunteerIsNull(long id, Pageable pageable, String searchWord) {
        List<Event> notParticipatedEvents = this.eventRepository.findWhereCurrentUserNotVolunteering(id,searchWord);
        List<Event> myEvents = this.eventRepository.findByVolunteer(id);
        List<EventViewModel> eventViewModels = new ArrayList<>();
        for (Event notParticipatedEvent : notParticipatedEvents) {
            boolean hasEvent = false;
            for (Event myEvent : myEvents) {
                if (notParticipatedEvent.getId().equals(myEvent.getId())) {
                    hasEvent = true;
                    break;
                }
            }

            if (!hasEvent){
                EventViewModel eventViewModel = this.modelMapper.map(notParticipatedEvent, EventViewModel.class);
                eventViewModels.add(eventViewModel);
            }
        }

        int totalPages = eventViewModels.size() == VALUE_ZERO ? DEFAILT_VALUE : eventViewModels.size() / EVENTS_VALUE;

        return new PageImpl<>(eventViewModels, pageable, totalPages);
    }

    @Override
    public void update(EditEventModel editEventModel) {
        editEventModel.setActive(editEventModel.getActive().equalsIgnoreCase(String.valueOf(Status.ACTIVE)) ? "true" : "false");
        Event event = this.modelMapper.map(editEventModel, Event.class);
        Country country = this.countryRepository.findOneByCountryName(editEventModel.getCountryName());
        Funder funder = this.funderRepository.findOneByUsername(editEventModel.getFunderName());
        if (funder != null) {
            event.setFunder(funder);
        }

        Organization organization = this.organizationRepository.findOneByUsername(editEventModel.getOrganizationUsername());
        event.setOrganization(organization);
        event.setCountry(country);

        this.eventRepository.save(event);
    }

    @Override
    public void deleteEvent(long id) {
        Event event = this.eventRepository.findOne(id);
        if (event == null) {
            throw new EventNotFoundExeption(Errors.EVENT_NOT_FOUND);
        }

        this.eventRepository.delete(event);
    }

    @Override
    public Page<EventMyViewModel> findAllAdminView(Pageable pageable) {
        Page<Event> events = this.eventRepository.findAll(pageable);
        List<EventMyViewModel> eventMyViewModels = new ArrayList<>();
        for (Event event : events) {
                EventMyViewModel eventMyViewModel = this.modelMapper.map(event, EventMyViewModel.class);
                eventMyViewModels.add(eventMyViewModel);
        }

        return new PageImpl<>(eventMyViewModels, pageable, events.getTotalPages());
    }


}
