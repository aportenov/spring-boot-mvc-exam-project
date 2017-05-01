package com.volunteers.areas.users.entities;

import com.volunteers.areas.events.entities.Event;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="funders")
@DiscriminatorValue(value = "fun")
public class Funder extends AbstractUser {

    private String address;

    @Column(length=10000)
    private String logo;

    private String priority;

    private String website;

    @OneToMany(mappedBy = "funder")
    private Set<Event> events;

    public Funder() {
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}

