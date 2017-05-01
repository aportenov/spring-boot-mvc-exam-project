package com.volunteers.areas.users.serviceImpl;

import com.volunteers.areas.countries.entities.Country;
import com.volunteers.areas.events.entities.Event;
import com.volunteers.areas.users.entities.SocialUser;
import com.volunteers.areas.users.entities.Volunteer;
import com.volunteers.areas.users.services.VolunteerService;
import com.volunteers.enumerators.Provider;
import com.volunteers.areas.users.models.binding.RegisterVolunteerModel;
import com.volunteers.areas.users.models.view.VolunteerViewModelBasic;
import com.volunteers.errors.Errors;
import com.volunteers.errors.VolunteerNotFoundExeption;
import com.volunteers.areas.countries.repositories.CountryRepository;
import com.volunteers.areas.users.repositories.SocialUserRepository;
import com.volunteers.areas.users.repositories.VolunteerRepository;
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
import java.util.Set;

@Service
public class VolunteerServiceImpl implements VolunteerService {

    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final VolunteerRepository volunteerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;
    private final SocialUserRepository socialUserRepository;

    public VolunteerServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper,
                                VolunteerRepository volunteerRepository,
                                BCryptPasswordEncoder bCryptPasswordEncoder,
                                RoleService roleService,
                                SocialUserRepository socialUserRepository) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.volunteerRepository = volunteerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
        this.socialUserRepository = socialUserRepository;
    }


    @Override
    @Transactional
    public void register(RegisterVolunteerModel registerVolunteerModel) {
        Volunteer volunteer = this.modelMapper.map(registerVolunteerModel, Volunteer.class);
        String encryptedPassword = this.bCryptPasswordEncoder.encode(registerVolunteerModel.getPassword());
        volunteer.setCountry(this.countryRepository.findOneByCountryName(registerVolunteerModel.getCountryName()));
        volunteer.setPassword(encryptedPassword);
        volunteer.setAccountNonExpired(true);
        volunteer.setAccountNonLocked(true);
        volunteer.setCredentialsNonExpired(true);
        volunteer.setEnabled(true);
        volunteer.addRole(this.roleService.getDefaultRole());

        this.volunteerRepository.save(volunteer);
    }

    @Override
    public List<VolunteerViewModelBasic> findAllByEventId(Long eventId) {
        List<Volunteer> volunteers = this.volunteerRepository.findByEventId(eventId);
        List<VolunteerViewModelBasic> volunteerViewModels = new ArrayList<>();
        for (Volunteer volunteer : volunteers) {
            VolunteerViewModelBasic volunteerViewModel = this.modelMapper.map(volunteer, VolunteerViewModelBasic.class);
            volunteerViewModels.add(volunteerViewModel);
        }

        return volunteerViewModels;
    }

    @Override
    public Page<VolunteerViewModelBasic> findAll(Pageable pageable) {
        Page<Volunteer> volunteers = this.volunteerRepository.findAll(pageable);
        List<VolunteerViewModelBasic> volunteerViewModelBasics = new ArrayList<>();
        for (Volunteer volunteer : volunteers) {
            VolunteerViewModelBasic volunteerViewModelBasic = this.modelMapper.map(volunteer, VolunteerViewModelBasic.class);
            volunteerViewModelBasics.add(volunteerViewModelBasic);
        }

        return new PageImpl<>(volunteerViewModelBasics, pageable , volunteers.getTotalPages());
    }

    @Override
    public RegisterVolunteerModel findOne(long id) {
        Volunteer volunteer = this.volunteerRepository.findOne(id);
        RegisterVolunteerModel registerVolunteerModel = null;
        if (volunteer == null) {
            throw new VolunteerNotFoundExeption(Errors.VOLUNTEER_NOT_FOUND);
        }

        registerVolunteerModel = this.modelMapper.map(volunteer, RegisterVolunteerModel.class);

        return registerVolunteerModel;
    }

    @Override
    @Transactional
    public void update(RegisterVolunteerModel registerVolunteerModel) {
        Volunteer isSocial = this.volunteerRepository.findOne(registerVolunteerModel.getId());
        Country country = this.countryRepository.findOneByCountryName(registerVolunteerModel.getCountryName());
        Set<Event> events = isSocial.getEvents();
        if (isSocial instanceof SocialUser) {
            SocialUser volunteer = this.modelMapper.map(registerVolunteerModel, SocialUser.class);
            volunteer.setImage(((SocialUser) isSocial).getImage());
            volunteer.addRole(this.roleService.getSocialRole());
            volunteer.setProvider(((SocialUser) isSocial).getProvider().equals(Provider.FACEBOOK) ? Provider.FACEBOOK : Provider.TWITTER );
            volunteer.setEvents(events);
            volunteer.setCountry(country);
            if (volunteer.getPassword().trim().equals("")) {
                String currPassword = isSocial.getPassword();
                volunteer.setPassword(currPassword);
            }


            this.socialUserRepository.save(volunteer);

        } else {
            Volunteer volunteer = this.modelMapper.map(registerVolunteerModel, Volunteer.class);
            volunteer.setEvents(events);
            volunteer.setCountry(country);
            if (volunteer.getPassword().trim().equals("")) {
                String currPassword = isSocial.getPassword();
                volunteer.setPassword(currPassword);
            }

            this.volunteerRepository.save(volunteer);
        }
    }

}
