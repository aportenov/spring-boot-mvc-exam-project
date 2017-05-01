package com.volunteers.areas.countries.controllers;

import com.volunteers.areas.countries.models.binding.RegisterCountryModel;
import com.volunteers.areas.countries.models.view.CountryViewModel;
import com.volunteers.areas.countries.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("")
    public ResponseEntity<List<CountryViewModel>> getCountries(){
        List<CountryViewModel> countries = this.countryService.findAllJson();

        if(countries == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        ResponseEntity<List<CountryViewModel>> responseEntity
                = new ResponseEntity(countries, HttpStatus.OK);

        return responseEntity;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("all")
    public String showCountries(Model model,@PageableDefault(size = 50) Pageable pageable){
        Page<CountryViewModel> countryViewModels = this.countryService.findAllCountries(pageable);
        model.addAttribute("countries", countryViewModels);

        return "/countries/show-countries";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("{countryId}")
    public ResponseEntity<CountryViewModel> getCountry(@PathVariable long countryId){
        CountryViewModel countryViewModel = this.countryService.findOne(countryId);
        ResponseEntity<CountryViewModel> responseEntity
                = new ResponseEntity(countryViewModel, HttpStatus.OK);

        return responseEntity;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("add")
    public ResponseEntity addCountry(@RequestBody RegisterCountryModel registerCountryModel) {
        this.countryService.createCountry(registerCountryModel);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("update/{itemId}")
    public ResponseEntity update(@PathVariable long itemId, @RequestBody CountryViewModel countryViewModel){
        this.countryService.updateItem(itemId, countryViewModel);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity delete(@PathVariable long itemId){
        this.countryService.deleteById(itemId);

        return new ResponseEntity(HttpStatus.OK);
    }
}
