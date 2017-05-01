package com.volunteers.areas.events.models.binding;

public class VolunteerAndEventModel {

    private long volunteerId;

    private long eventId;

    public long getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(long volunteerId) {
        this.volunteerId = volunteerId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }
}
