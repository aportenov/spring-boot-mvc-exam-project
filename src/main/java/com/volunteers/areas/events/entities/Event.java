package com.volunteers.areas.events.entities;

import com.volunteers.areas.countries.entities.Country;
import com.volunteers.areas.users.entities.Funder;
import com.volunteers.areas.users.entities.Organization;
import com.volunteers.areas.users.entities.Volunteer;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal requiredBudget;

    private String objectives;

    private float latitude;

    private float longitude;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "funder_id")
    private Funder funder;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "volunteers_events",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "volunteer_id"))
    private Set<Volunteer> volunteers;

    private Date startDate;

    private Date endDate;

    private boolean isActive;

    public Event() {
        this.setVolunteers(new HashSet<>());
    }

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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Funder getFunder() {
        return funder;
    }

    public void setFunder(Funder funder) {
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Set<Volunteer> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(Set<Volunteer> volunteers) {
        this.volunteers = volunteers;
    }

    public void addVolunteer(Volunteer volunteer) {
        this.getVolunteers().add(volunteer);
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
