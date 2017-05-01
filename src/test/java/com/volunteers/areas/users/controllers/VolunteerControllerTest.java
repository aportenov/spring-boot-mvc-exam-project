package com.volunteers.areas.users.controllers;

import com.volunteers.areas.users.models.binding.RegisterVolunteerModel;
import com.volunteers.areas.users.models.view.VolunteerViewModelBasic;
import com.volunteers.areas.users.services.VolunteerService;
import com.volunteers.config.CsrfRequestPostProcessor;
import com.volunteers.errors.VolunteerNotFoundExeption;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(VolunteerController.class)
@EnableSpringDataWebSupport
@ActiveProfiles("test")
public class VolunteerControllerTest {

    private static final String ID = "id";
    private static final String Name = "name";
    private static final String USERNAME = "username";
    private static final String COUNTRY_NAME = "countryName";
    private static final String VOLUNTEER_NAME = "name";
    private static final String VOLUNTEER_USERNAME = "name@name.bg";
    private static final String VOLUNTEER_COUNTRY_NAME = "Bulgaria";
    private static final long VOLUNTEER_ID = 1;
    private static final String VOLUNTEERS_URL = "/volunteers/all";
    private static final String EXPECTED_URL = "/volunteers/show-volunteers";
    private static final String VOLUNTEERS_ATTRIBUTE = "volunteers";
    private static final int EXPECTED_CALL = 1;
    private static final String SINGLE_VOLUNTEER_URL = "/volunteer/";
    private static final String EXPECTED_SINGLE_VOLUNTEER_URL = "/volunteers/edit-volunteer";
    private static final String REGISTER_MODEL = "registerVolunteerModel";
    private static final String GENDER = "gender";
    private static final String VOLUNTEER_GENDER = "male";
    private static final String REGISTER_URL = "/register/volunteer";
    private static final long INVAID_VOLUNTEER_ID = -1;
    private static final String PASSWORD = "PASSWORD";
    private static final String EXPECTED_PASSWORD = "Mypassword1";
    private static final String EXPECTED_ERROR_PAGE = "/error";
    private static final String REDIRECTED_URL = "/";
    private static final String REDIRECT = "redirect:";
    private static final String AGE = "age";
    private static final String VOLUNTEER_AGE = "18";
    private static final String REGISTER_ERROR_URL = "/register";
    private static final long EVENT_ID = 1;
    private static final String VOLUNTEERS_PER_EVENT_URL = "/volunteers/event/";
    private static final String DOLLAR_SIGN = "$[0].";
    private static final String NAME = "name" ;

    @MockBean
    private VolunteerService volunteerService;

    @Captor
    private ArgumentCaptor<Pageable> captor;

    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();

        VolunteerViewModelBasic volunteerViewModelBasic = new VolunteerViewModelBasic();
        volunteerViewModelBasic.setName(VOLUNTEER_NAME);
        volunteerViewModelBasic.setUsername(VOLUNTEER_USERNAME);
        volunteerViewModelBasic.setCountryName(VOLUNTEER_COUNTRY_NAME);
        volunteerViewModelBasic.setId(VOLUNTEER_ID);

        Page<VolunteerViewModelBasic> volunteerViewModelBasics = new PageImpl<>(Arrays.asList(volunteerViewModelBasic));
        when(this.volunteerService.findAll(this.captor.capture())).thenReturn(volunteerViewModelBasics);

        RegisterVolunteerModel registerVolunteerModel = new RegisterVolunteerModel();
        registerVolunteerModel.setName(VOLUNTEER_NAME);
        registerVolunteerModel.setCountryName(VOLUNTEER_COUNTRY_NAME);
        registerVolunteerModel.setUsername(VOLUNTEER_USERNAME);
        registerVolunteerModel.setId(VOLUNTEER_ID);
        registerVolunteerModel.setGender(VOLUNTEER_GENDER);
        when(this.volunteerService.findOne(VOLUNTEER_ID)).thenReturn(registerVolunteerModel);
        when(this.volunteerService.findOne(INVAID_VOLUNTEER_ID)).thenThrow(VolunteerNotFoundExeption.class);

        VolunteerViewModelBasic volunteerViewModel = new VolunteerViewModelBasic();
        volunteerViewModel.setId(VOLUNTEER_ID);
        volunteerViewModel.setCountryName(VOLUNTEER_COUNTRY_NAME);
        volunteerViewModel.setName(VOLUNTEER_NAME);
        volunteerViewModel.setUsername(VOLUNTEER_USERNAME);
        List<VolunteerViewModelBasic> volunteerViewModelList = new ArrayList<>(Arrays.asList(volunteerViewModel));
        when(this.volunteerService.findAllByEventId(EVENT_ID)).thenReturn(volunteerViewModelList);
    }


    @Test
    public void showVolunteersPage_ShouldReturnValidStatusAndVolunteersPage() throws Exception {
        this.mockMvc.perform(get(VOLUNTEERS_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(EXPECTED_URL));

    }

    @Test
    public void verifyVolunteersServiceIsCalled_ShouldReturnValidCall() throws Exception {
        //Act
        this.mockMvc.perform(get(VOLUNTEERS_URL));
        //Assert service call
        verify(this.volunteerService, times(EXPECTED_CALL)).findAll(captor.capture());
    }

    @Test
    public void verifyVolunteersPageReturnValidModelToView_ShouldReturnValidVolunteerViewModelBasic() throws Exception {
        this.mockMvc.perform(get(VOLUNTEERS_URL))
                .andExpect(model().attribute(VOLUNTEERS_ATTRIBUTE, hasItem(
                        allOf(
                                hasProperty(ID, is(VOLUNTEER_ID)),
                                hasProperty(Name, is(VOLUNTEER_NAME)),
                                hasProperty(COUNTRY_NAME, is(VOLUNTEER_COUNTRY_NAME)),
                                hasProperty(USERNAME, is(VOLUNTEER_USERNAME))
                        )
                )));

    }

    @Test
    public void showSingleVolunteerPage_ShouldReturnValidResponseAndView() throws Exception {
        mockMvc.perform(
                get(SINGLE_VOLUNTEER_URL + VOLUNTEER_ID)
                        .with(CsrfRequestPostProcessor.csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name(EXPECTED_SINGLE_VOLUNTEER_URL));
    }

    @Test
    public void verifySingleVolunteerPageModelAttribute_ShouldReturnValidModelAttribute() throws Exception {
        this.mockMvc.perform(
                get(SINGLE_VOLUNTEER_URL + VOLUNTEER_ID)
                        .with(CsrfRequestPostProcessor.csrf()))
                .andExpect(model().attribute(REGISTER_MODEL, hasProperty(ID, is(VOLUNTEER_ID))))
                .andExpect(model().attribute(REGISTER_MODEL, hasProperty(USERNAME, is(VOLUNTEER_USERNAME))))
                .andExpect(model().attribute(REGISTER_MODEL, hasProperty(Name, is(VOLUNTEER_NAME))))
                .andExpect(model().attribute(REGISTER_MODEL, hasProperty(COUNTRY_NAME, is(VOLUNTEER_COUNTRY_NAME))))
                .andExpect(model().attribute(REGISTER_MODEL, hasProperty(GENDER, is(VOLUNTEER_GENDER))));

    }

    @Test
    public void showVolunteersPageWhenGivenWrongId_ShouldReturnErrorPage() throws Exception {
        //Act
        this.mockMvc.perform(get(SINGLE_VOLUNTEER_URL + INVAID_VOLUNTEER_ID))
                .andExpect(view().name(EXPECTED_ERROR_PAGE));

    }

    @Test
    public void verifyRegisterVolunteerWhenGivenValidParams_ShouldReturnValidStatusAndView() throws Exception {

        this.mockMvc.perform(post(REGISTER_URL)
                .param(USERNAME, VOLUNTEER_USERNAME)
                .param(PASSWORD, EXPECTED_PASSWORD)
                .param(GENDER, VOLUNTEER_GENDER)
                .param(Name, VOLUNTEER_NAME)
                .param(COUNTRY_NAME, VOLUNTEER_COUNTRY_NAME)
                .param(AGE, VOLUNTEER_AGE)
                .with(CsrfRequestPostProcessor.csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT + REDIRECTED_URL))
                .andExpect(redirectedUrl(REDIRECTED_URL))
                .andExpect(model().hasNoErrors());

        ArgumentCaptor<RegisterVolunteerModel> captor = ArgumentCaptor.forClass(RegisterVolunteerModel.class);
        verify(volunteerService).register(captor.capture());
        RegisterVolunteerModel volunteerModel = captor.getValue();
        assertEquals(VOLUNTEER_USERNAME, volunteerModel.getUsername());

    }

    @Test
    public void verifyRegisterVolunteerWhenGivenInValidParams_ShouldReturnToRegisterPage() throws Exception {

        this.mockMvc.perform(post(REGISTER_URL)
                .param(USERNAME, VOLUNTEER_USERNAME)
                .param(PASSWORD, EXPECTED_PASSWORD)
                .param(GENDER, VOLUNTEER_GENDER)
                .with(CsrfRequestPostProcessor.csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT + REGISTER_ERROR_URL))
                .andExpect(redirectedUrl(REGISTER_ERROR_URL));
    }

    @Test
    public void getJSONVolunteersPerEvent_ShouldReturnValidData() throws Exception {
        this.mockMvc.perform(get(VOLUNTEERS_PER_EVENT_URL + EVENT_ID)
                .with(CsrfRequestPostProcessor.csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath(DOLLAR_SIGN + ID, is((int) VOLUNTEER_ID)))
                .andExpect(jsonPath(DOLLAR_SIGN + USERNAME, is(VOLUNTEER_USERNAME)))
                .andExpect(jsonPath(DOLLAR_SIGN + NAME, is(VOLUNTEER_NAME)))
                .andExpect(jsonPath(DOLLAR_SIGN + COUNTRY_NAME, is(VOLUNTEER_COUNTRY_NAME)));


    }


}
