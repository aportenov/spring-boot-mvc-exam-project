package com.volunteers.areas.events.services;

import com.volunteers.areas.events.models.binding.EditEventModel;
import com.volunteers.areas.events.models.binding.RegisterEventModel;
import com.volunteers.areas.events.models.view.EventMyViewModel;
import com.volunteers.areas.events.models.view.EventViewModel;
import com.volunteers.areas.events.models.view.EventViewModelFull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {

    Page<EventViewModel> findAll(Pageable pageable, String searchWord);

    void addSingleVolunteer(long volunteerId, long eventId);

    Page<EventMyViewModel> findAllByOrganization(long organizationId, Pageable pageable);

    EventViewModelFull findById(long eventId);

    void create(RegisterEventModel registerEventModel, String organizationName);

    void addSingleFunder(long funderId, long eventId);

    Page<EventMyViewModel> findAllByVolunteer(long id, Pageable pageable);

    Page<EventMyViewModel> findAllByFunder(long id, Pageable pageable);

    Page<EventViewModel> findWhereCurrentVolunteerIsNull(long id, Pageable pageable, String searchWord);

    void update(EditEventModel editEventModel);

    void deleteEvent(long id);

    Page<EventMyViewModel> findAllAdminView(Pageable pageable);
}







