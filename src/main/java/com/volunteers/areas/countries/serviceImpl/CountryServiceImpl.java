package com.volunteers.areas.countries.serviceImpl;

import com.volunteers.areas.countries.entities.Country;
import com.volunteers.areas.countries.models.view.CountryViewModel;
import com.volunteers.areas.countries.models.binding.RegisterCountryModel;
import com.volunteers.areas.countries.repositories.CountryRepository;
import com.volunteers.areas.countries.services.CountryService;
import com.volunteers.errors.CountryNotFoundEexeption;
import com.volunteers.errors.Errors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final ModelMapper modelMapper;
    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(ModelMapper modelMapper, CountryRepository countryRepository) {
        this.modelMapper = modelMapper;
        this.countryRepository = countryRepository;
    }

    @Override
    public List<CountryViewModel> findAllJson() {
        List<Country> countries = this.countryRepository.findAllCountries();
        List<CountryViewModel> countryViewModels = new ArrayList<>();
        for (Country country : countries) {
            countryViewModels.add(this.modelMapper.map(country, CountryViewModel.class));
        }

        return countryViewModels;
    }

    @Override
    public Page<CountryViewModel> findAllCountries(Pageable pageable) {
         Page<Country> countries = this.countryRepository.findAll(pageable);
         List<CountryViewModel> countryViewModels = new ArrayList<>();
        for (Country country : countries) {
            countryViewModels.add(this.modelMapper.map(country, CountryViewModel.class));
        }

         return new PageImpl(countryViewModels,pageable, countries.getTotalElements());
    }

    @Override
    public CountryViewModel findOne(long countryId) {
        Country country = this.countryRepository.findOne(countryId);
        CountryViewModel countryViewModel = null;
        if (country == null) {
            throw new CountryNotFoundEexeption(Errors.COUNTRY_NOT_FOUND);
        }

        countryViewModel = this.modelMapper.map(country, CountryViewModel.class);

        return countryViewModel;
    }

    @Override
    public void updateItem(long itemId, CountryViewModel countryViewModel) {
        Country country = this.countryRepository.findOne(itemId);
        if (country != null) {
            country.setCountryCode(countryViewModel.getCountryCode());
            country.setCountryName(countryViewModel.getCountryName());
            this.countryRepository.save(country);
        }

    }

    @Override
    public void deleteById(long itemId) {
        Country country = this.countryRepository.findOne(itemId);
        if (country != null){
            this.countryRepository.delete(country);
        }

    }

    @Override
    public void createCountry(RegisterCountryModel registerCountryModel) {
        Country country = new Country();
        country.setCountryCode(registerCountryModel.getCountryCode());
        country.setCountryName(registerCountryModel.getCountryName());

        this.countryRepository.save(country);
    }

}
