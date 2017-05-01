package com.volunteers.areas.countries.models.binding;

import org.hibernate.validator.constraints.NotBlank;

public class RegisterCountryModel {

    @NotBlank
    private String countryCode;

    @NotBlank
    private String countryName;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
