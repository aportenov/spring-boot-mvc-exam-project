package com.volunteers.areas.events.repositories;

import com.volunteers.areas.events.entities.Event;
import com.volunteers.areas.users.entities.Organization;
import com.volunteers.areas.users.repositories.OrganizationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class EventRepositoryTest {

    private static final String SEARCH_WORD_TRUE = "True";
    private static final String EMPTY_SEARCH_WORD = "";
    private static final String SEARCH_WORD_FALSE = "False";
    private static final String EVENT_NAME_TRUE = "True test'";
    private static final String EVENT_NAME_FALSE = "True test'";
    private static final int EXPECTED_LIST_SIZE_TRUE = 1;
    private static final int EXPECTED_LIST_SIZE_FALSE = 0;

    @MockBean
    private Pageable pageable;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private EventRepository eventRepository;

    @MockBean
    private OrganizationRepository organizationRepository;


    @Before
    public void SetUp() throws Exception {

        Event trueEvent = new Event();
        trueEvent.setName(EVENT_NAME_TRUE);
        trueEvent.setActive(true);

        this.testEntityManager.persist(trueEvent);

        Event falseEvent = new Event();
        falseEvent.setName(EVENT_NAME_FALSE);
        falseEvent.setActive(false);

        this.testEntityManager.persist(falseEvent);
    }


    @Test
    @Transactional
    public void findEventByGivenOrganizationId_ShouldReturnCorrectSize() throws Exception {
        //Act
        Event trueEvent = new Event();
        Organization organization = new Organization();
        Organization org = this.testEntityManager.persistFlushFind(organization);
        trueEvent.setOrganization(org);
        this.testEntityManager.persist(trueEvent);

        Page<Event> events = this.eventRepository.findByOrganizationId(org.getId(), pageable);

        //Assert
        assertEquals(EXPECTED_LIST_SIZE_TRUE, events.getTotalElements());
    }

    @Test
    public void findEventWhenActiveIsTrueAndSearchWordIsPresent_ShouldReturnCorrectEvent() throws Exception {
        //Act
        Page<Event> eventList = this.eventRepository.findByIsActiveTrue(SEARCH_WORD_TRUE, pageable);

        //Assert
        assertEquals(EXPECTED_LIST_SIZE_TRUE,eventList.getTotalElements());

    }

    @Test
    public void findEventWhenActiveIsTrueAndSearchWordIsNull_ShouldReturnCorrectEvent() throws Exception {
        //Act
        Page<Event> eventList = this.eventRepository.findByIsActiveTrue(EMPTY_SEARCH_WORD, pageable);

        //Assert
        assertEquals(EXPECTED_LIST_SIZE_TRUE, eventList.getTotalElements());

    }

    @Test
    public void findEventWhenActiveIsTrueAndWrongSearchWordIsGiven_ShouldReturnEmptyList() throws Exception {
        //Act
        Page<Event> eventList = this.eventRepository.findByIsActiveTrue(SEARCH_WORD_FALSE, pageable);

        //Assert
        assertEquals(EXPECTED_LIST_SIZE_FALSE, eventList.getTotalElements());

    }

}