package com.volunteers.areas.events.models.binding;

import com.volunteers.anotations.EndDateBeforeStartDate;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@EndDateBeforeStartDate
public class RegisterEventModel {

    @NotBlank
    private String name;

    @NotBlank
    private String countryName;

    @NotNull
    private BigDecimal requiredBudget;

    @NotBlank
    private String objectives;

    private Float latitude;

    private Float longitude;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Date cannot be in the past")
    private Date startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private String active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRequiredBudget() {
        return requiredBudget;
    }

    public void setRequiredBudget(BigDecimal requiredBudget) {
        this.requiredBudget = requiredBudget;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

}
