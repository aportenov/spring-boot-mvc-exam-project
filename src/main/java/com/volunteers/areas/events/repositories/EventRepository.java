package com.volunteers.areas.events.repositories;

import com.volunteers.areas.events.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT e FROM Event AS e join  e.organization WHERE e.organization.id = :organizationId")
    Page<Event> findByOrganizationId(@Param("organizationId") long organizationId, Pageable pageable);

    @Query(value = "SELECT e FROM Event AS e WHERE e.isActive = true AND e.name LIKE CONCAT('%', :searchWord, '%')")
    Page<Event> findByIsActiveTrue(@Param("searchWord") String searchWord, Pageable pageable);

    @Query(value = "SELECT e FROM Event AS e left join e.volunteers AS v " +
                                            "  WHERE e.name LIKE CONCAT('%', :searchWord, '%')" +
                                            "  AND (v.id is null OR v.id <> :id)")
    List<Event> findWhereCurrentUserNotVolunteering(@Param("id") long id, @Param("searchWord")  String searchWord);

    @Query(value = "SELECT e FROM Event AS e left join e.volunteers as v WHERE v.id = :id")
    Page<Event> findByVolunteer(@Param("id") long id, Pageable pageable);

    @Query(value = "SELECT e FROM Event  AS e join e.funder AS f WHERE f.id = :id ")
    Page<Event> findByFunder(@Param("id") long id, Pageable pageable);

    List<Event> findByIsActiveTrue(String search_word);
}

