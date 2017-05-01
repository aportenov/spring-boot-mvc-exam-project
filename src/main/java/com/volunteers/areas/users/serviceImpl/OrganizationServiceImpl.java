package com.volunteers.areas.users.serviceImpl;

import com.volunteers.areas.users.services.OrganizationService;
import com.volunteers.areas.users.entities.Organization;
import com.volunteers.areas.users.models.view.OrganizationViewModel;
import com.volunteers.areas.users.models.binding.RegisterOrganizationModel;
import com.volunteers.errors.Errors;
import com.volunteers.errors.OrganizationNotFoundExeption;
import com.volunteers.areas.countries.repositories.CountryRepository;
import com.volunteers.areas.users.repositories.OrganizationRepository;
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
public class OrganizationServiceImpl implements OrganizationService {

    private final ModelMapper modelMapper;
    private final CountryRepository countryRepository;
    private final OrganizationRepository organizationRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;

    public OrganizationServiceImpl(ModelMapper modelMapper,
                                   CountryRepository countryRepository,
                                   OrganizationRepository organizationRepository,
                                   BCryptPasswordEncoder bCryptPasswordEncoder,
                                   RoleService roleService) {
        this.modelMapper = modelMapper;
        this.countryRepository = countryRepository;
        this.organizationRepository = organizationRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
    }


    @Override
    public void register(RegisterOrganizationModel registerOrganizationModel) {
        Organization organization = this.modelMapper.map(registerOrganizationModel, Organization.class);
        String encryptedPassword = this.bCryptPasswordEncoder.encode(registerOrganizationModel.getPassword());
        organization.setCountry(this.countryRepository.findOneByCountryName(registerOrganizationModel.getCountryName()));
        organization.setPassword(encryptedPassword);
        organization.setAccountNonExpired(true);
        organization.setAccountNonLocked(true);
        organization.setCredentialsNonExpired(true);
        organization.setEnabled(true);
        organization.addRole(this.roleService.getOrganizationRole());

        this.organizationRepository.save(organization);
    }

    @Override
    public Page<OrganizationViewModel> findAll(Pageable pageable) {
        Page<Organization> organizations = this.organizationRepository.findAll(pageable);
        List<OrganizationViewModel> organizizationViewModels = new ArrayList<>();
        for (Organization organization : organizations) {
            organizizationViewModels.add(this.modelMapper.map(organization, OrganizationViewModel.class));
        }

        return new PageImpl<>(organizizationViewModels,pageable, organizations.getTotalPages());
    }

    @Override
    public RegisterOrganizationModel findOne(long id) {
        Organization organization = this.organizationRepository.findOne(id);
        RegisterOrganizationModel registerOrganizationModel = null;
        if (organization == null) {
            throw new OrganizationNotFoundExeption(Errors.ORGANIZATION_NOT_FOUND);
        }

        registerOrganizationModel = this.modelMapper.map(organization, RegisterOrganizationModel.class);

        return registerOrganizationModel;
    }


    @Override
    @Transactional
    public void update(RegisterOrganizationModel registerOrganizationModel) {
        Organization organization = this.modelMapper.map(registerOrganizationModel, Organization.class);
        if (organization.getPassword().trim().equals("")) {
            Organization currPassword = this.organizationRepository.findOne(registerOrganizationModel.getId());
            organization.setPassword(currPassword.getPassword());
        }

        this.organizationRepository.save(organization);
    }
}
