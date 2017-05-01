package com.volunteers.areas.users.services;

import com.volunteers.areas.users.models.binding.RegisterVolunteerModel;
import com.volunteers.areas.users.models.view.VolunteerViewModelBasic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VolunteerService{

    void register(RegisterVolunteerModel volunteer);

    List<VolunteerViewModelBasic> findAllByEventId(Long eventId);

    Page<VolunteerViewModelBasic> findAll(Pageable pageable);

    RegisterVolunteerModel findOne(long id);

    void update(RegisterVolunteerModel registerVolunteerModel);
}


