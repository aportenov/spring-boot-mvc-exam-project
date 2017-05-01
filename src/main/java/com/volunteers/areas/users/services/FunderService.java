package com.volunteers.areas.users.services;


import com.volunteers.areas.users.models.view.FunderViewModel;
import com.volunteers.areas.users.models.view.FunderViewModelBasic;
import com.volunteers.areas.users.models.binding.RegisterFunderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FunderService {

    void register(RegisterFunderModel registerFunderModel);

    Page<FunderViewModelBasic> findAll(Pageable pageable);

    FunderViewModel findOne(long id);

    void update(RegisterFunderModel registerFunderModel);
}



