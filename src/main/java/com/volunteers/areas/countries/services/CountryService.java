package com.volunteers.areas.countries.services;

import com.volunteers.areas.countries.models.view.CountryViewModel;
import com.volunteers.areas.countries.models.binding.RegisterCountryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CountryService {

    List<CountryViewModel> findAllJson();

    Page<CountryViewModel> findAllCountries(Pageable pageable);

    CountryViewModel findOne(long countryId);

    void updateItem(long itemId, CountryViewModel countryViewModel);

    void deleteById(long itemId);

    void createCountry(RegisterCountryModel registerCountryModel);
}
