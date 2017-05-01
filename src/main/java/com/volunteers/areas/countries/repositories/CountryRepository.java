package com.volunteers.areas.countries.repositories;

import com.volunteers.areas.countries.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Country findOneByCountryName(String country);

    @Query(value = "SELECT c FROM Country AS c")
    List<Country> findAllCountries();
}
