package com.volunteers.areas.users.services;

import com.volunteers.areas.users.models.view.OrganizationViewModel;
import com.volunteers.areas.users.models.binding.RegisterOrganizationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrganizationService {

    void register(RegisterOrganizationModel registerOrganizationModel);

    Page<OrganizationViewModel> findAll(Pageable pageable);

    RegisterOrganizationModel findOne(long id);

    void update(RegisterOrganizationModel registerOrganizationModel);
}


