package com.volunteers.areas.events.models.view;
import java.util.Date;

public class EventMyViewModel {

    private Long id;

    private String name;

    private String countryName;

    private String organizationName;

    private String organizationUsername;

    private String organizationWebsite;

    private String funderUsername;

    private String funderName;

    private String funderWebsite;

    private Float latitude;

    private Float longitude;

    private boolean isActive;

    private Date startDate;

    private Date endDate;

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

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationUsername() {
        return organizationUsername;
    }

    public void setOrganizationUsername(String organizationUsername) {
        this.organizationUsername = organizationUsername;
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

    public String getOrganizationWebsite() {
        return organizationWebsite;
    }

    public void setOrganizationWebsite(String organizationWebsite) {
        this.organizationWebsite = organizationWebsite;
    }

    public String getFunderUsername() {
        return funderUsername;
    }

    public void setFunderUsername(String funderUsername) {
        this.funderUsername = funderUsername;
    }

    public String getFunderName() {
        return funderName;
    }

    public void setFunderName(String funderName) {
        this.funderName = funderName;
    }

    public String getFunderWebsite() {
        return funderWebsite;
    }

    public void setFunderWebsite(String funderWebsite) {
        this.funderWebsite = funderWebsite;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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
}

