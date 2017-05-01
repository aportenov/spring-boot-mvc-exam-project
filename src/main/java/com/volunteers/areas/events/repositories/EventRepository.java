package com.volunteers.areas.events.repositories;

import com.volunteers.areas.events.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT e FROM Event AS e join fetch  e.organization WHERE e.organization.id = :organizationId")
    List<Event> findByOrganizationId(@Param("organizationId")long organizationId);

    @Query(value = "SELECT e FROM Event AS e WHERE e.isActive = true AND e.name LIKE CONCAT('%', :searchWord, '%')")
    List<Event> findByIsActiveTrue(@Param("searchWord") String searchWord);

    @Query(value = "SELECT e FROM Event AS e left join fetch e.volunteers AS v " +
                                            "  WHERE e.name LIKE CONCAT('%', :searchWord, '%')" +
                                            "  AND (v.id is null OR v.id <> :id)")
    List<Event> findWhereCurrentUserNotVolunteering(@Param("id") long id, @Param("searchWord")  String searchWord);

    @Query(value = "SELECT e FROM Event AS e left join fetch e.volunteers as v WHERE v.id = :id")
    List<Event> findByVolunteer(@Param("id") long id);

    @Query(value = "SELECT e FROM Event  AS e join fetch e.funder AS f WHERE f.id = :id ")
    List<Event> findByFunder(@Param("id") long id);
}
