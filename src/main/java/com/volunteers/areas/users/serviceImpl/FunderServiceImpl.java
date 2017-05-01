package com.volunteers.areas.users.serviceImpl;

import com.volunteers.areas.users.services.FunderService;
import com.volunteers.areas.users.entities.Funder;
import com.volunteers.areas.users.models.view.FunderViewModel;
import com.volunteers.areas.users.models.view.FunderViewModelBasic;
import com.volunteers.areas.users.models.binding.RegisterFunderModel;
import com.volunteers.errors.Errors;
import com.volunteers.errors.FunderNotFoundExeption;
import com.volunteers.areas.countries.repositories.CountryRepository;
import com.volunteers.areas.users.repositories.FunderRepository;
import com.volunteers.areas.users.services.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class FunderServiceImpl implements FunderService {

    private final ModelMapper modelMapper;
    private final CountryRepository countryRepository;
    private final FunderRepository funderRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;


    public FunderServiceImpl(ModelMapper modelMapper,
                             CountryRepository countryRepository,
                             FunderRepository funderRepository,
                             BCryptPasswordEncoder bCryptPasswordEncoder,
                             RoleService roleService) {
        this.modelMapper = modelMapper;
        this.countryRepository = countryRepository;
        this.funderRepository = funderRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
    }

    @Override
    public void register(RegisterFunderModel registerFunderModel) {
        Funder funder = this.modelMapper.map(registerFunderModel, Funder.class);
        String encryptedPassword = this.bCryptPasswordEncoder.encode(registerFunderModel.getPassword());
        funder.setCountry(this.countryRepository.findOneByCountryName(registerFunderModel.getCountryName()));
        funder.setPassword(encryptedPassword);
        funder.setAccountNonExpired(true);
        funder.setAccountNonLocked(true);
        funder.setCredentialsNonExpired(true);
        funder.setEnabled(true);
        funder.addRole(this.roleService.getFunderRole());

        this.funderRepository.save(funder);
    }

    @Override
    public Page<FunderViewModelBasic> findAll(Pageable pageable) {
        Page<Funder> funders = this.funderRepository.findAll(pageable);
        List<FunderViewModelBasic> funderViewModelBasics = new ArrayList<>();
        for (Funder funder : funders) {
            funderViewModelBasics.add(this.modelMapper.map(funder, FunderViewModelBasic.class));
        }

        return new PageImpl<>(funderViewModelBasics, pageable , funders.getTotalPages());
    }

    @Override
    public FunderViewModel findOne(long id) {
        Funder funder = this.funderRepository.findOne(id);
        FunderViewModel funderViewModel = null;
        if (funder == null) {
            throw new FunderNotFoundExeption(Errors.FUNDER_NOT_FOUND);
        }

        funderViewModel = this.modelMapper.map(funder, FunderViewModel.class);

        return funderViewModel;
    }

    @Override
    @Transactional
    public void update(RegisterFunderModel registerFunderModel) {
        Funder funder = this.modelMapper.map(registerFunderModel, Funder.class);
        if (funder.getPassword().trim().equals("")) {
            Funder currPassword = this.funderRepository.findOne(registerFunderModel.getId());
            funder.setPassword(currPassword.getPassword());
        }

        this.funderRepository.save(funder);
    }
}
