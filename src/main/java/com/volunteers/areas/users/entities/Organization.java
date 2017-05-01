package com.volunteers.areas.users.entities;

import com.volunteers.areas.events.entities.Event;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="organizations")
@DiscriminatorValue(value = "org")
public class Organization extends AbstractUser {

    private String address;

    private String website;

    @Column(length=10000)
    private String logo;

    @Column(length=10000)
    private String objectives;

    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER)
    private Set<Event> events;

    public Organization() {
        this.setEvents(new HashSet<>());
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
