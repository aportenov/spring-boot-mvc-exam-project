package com.volunteers.areas.users.entities;
import com.volunteers.areas.events.entities.Event;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "volunteers")
@DiscriminatorValue(value = "vol")
public class Volunteer extends AbstractUser {

    private Integer age;

    private String gender;

    @ManyToMany(mappedBy = "volunteers")
    private Set<Event> events;

    public Volunteer() {
        this.setEvents(new HashSet<>());
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }
}
