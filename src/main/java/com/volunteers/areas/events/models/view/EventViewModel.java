package com.volunteers.areas.events.models.view;

import com.volunteers.areas.users.models.view.FunderWebViewModel;
import com.volunteers.areas.users.models.view.OrganizationViewModel;

import java.math.BigDecimal;
import java.util.Date;


public class EventViewModel {

    private Long id;

    private String name;

    private String countryName;

    private BigDecimal requiredBudget;

    private String objectives;

    private OrganizationViewModel organization;

    private FunderWebViewModel funder;

    private Date startDate;

    private Date endDate;

    private float latitude;

    private float longitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
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

    public FunderWebViewModel getFunder() {
        return funder;
    }

    public void setFunder(FunderWebViewModel funder) {
        this.funder = funder;
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

    public OrganizationViewModel getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationViewModel organization) {
        this.organization = organization;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
