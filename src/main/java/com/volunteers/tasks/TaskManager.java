package com.volunteers.tasks;

import com.volunteers.areas.events.entities.Event;
import com.volunteers.areas.events.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class TaskManager {

    private final String SEARCH_WORD = "";

    private final EventRepository eventRepository;

    @Autowired
    public TaskManager(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    @Scheduled(cron = "* 59 23 * * *", zone = "Europe/Sofia")
    public void ArhiveEvents() {
        List<Event> eventList = this.eventRepository.findByIsActiveTrue(SEARCH_WORD);
        for (Event event : eventList) {
            if (event.getEndDate().after(new Date())) {
                continue;
            }

            event.setActive(false);
            this.eventRepository.save(event);
        }
    }
}
