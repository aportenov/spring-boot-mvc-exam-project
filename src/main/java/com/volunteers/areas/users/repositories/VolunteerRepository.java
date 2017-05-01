package com.volunteers.areas.users.repositories;

import com.volunteers.areas.users.entities.Volunteer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolunteerRepository extends UserRepository<Volunteer> {

    @Query(value = "SELECT v FROM Volunteer v join v.events e WHERE e.id =:eventId")
    List<Volunteer> findByEventId(@Param("eventId") Long eventId);
}
