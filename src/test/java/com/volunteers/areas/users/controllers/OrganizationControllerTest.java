package com.volunteers.areas.users.controllers;

import com.volunteers.areas.users.models.binding.RegisterOrganizationModel;
import com.volunteers.areas.users.models.view.OrganizationViewModel;
import com.volunteers.areas.users.services.OrganizationService;
import com.volunteers.config.CsrfRequestPostProcessor;
import com.volunteers.errors.OrganizationNotFoundExeption;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(OrganizationController.class)
@EnableSpringDataWebSupport
@ActiveProfiles("test")
public class OrganizationControllerTest {

    private static final String ID = "id";
    private static final String Name = "name";
    private static final String USERNAME = "username";
    private static final String COUNTRY_NAME = "countryName";
    private static final String ORGANIZATIONS_NAME = "name";
    private static final String ORGANIZATIONS_USERNAME = "name@name.bg";
    private static final String ORGANIZATIONS_COUNTRY_NAME = "Bulgaria";
    private static final String ORGANIZATIONS_LOGO = "logo";
    private static final String ORGANIZATIONS_ADDRESS = "72str. bla bla";
    private static final String ORGANIZATIONS_WEBSITE = "website";
    private static final long ORGANIZATIONS_ID = 1;
    private static final long INVALID_ORGANIZATIONS_ID = -1;
    private static final String ORGANIZATIONS_URL = "/organizations/all";
    private static final String EXPECTED_URL = "/organizations/show-organizations";
    private static final String ORGANIZATIONS_ATTRIBUTE = "organizations";
    private static final int EXPECTED_CALL = 1;
    private static final String WEBSITE = "website";
    private static final String LOGO = "logo";
    private static final String SINGLE_ORGANIZATION_URL = "/organization/";
    private static final String EXPECTED_SINGLE_ORGANIZATION_URL = "/organizations/edit-organization";
    private static final String REGISTER_MODEL = "registerOrganizationModel";
    private static final String EXPECTED_ERROR_PAGE = "/error";
    private static final String REGISTER_URL = "/register/organization";
    private static final String PASSWORD = "PASSWORD";
    private static final String EXPECTED_PASSWORD = "Mypassword1";
    private static final String REDIRECTED_URL = "/";
    private static final String REDIRECT = "redirect:";
    private static final String REGISTER_ERROR_URL = "/register";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";

    @MockBean
    private OrganizationService organizationService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Captor
    private ArgumentCaptor<Pageable> captor;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {

        OrganizationViewModel organization = new OrganizationViewModel();
        organization.setName(ORGANIZATIONS_NAME);
        organization.setUsername(ORGANIZATIONS_USERNAME);
        organization.setLogo(ORGANIZATIONS_ADDRESS);
        organization.setWebsite(ORGANIZATIONS_WEBSITE);
        organization.setId(ORGANIZATIONS_ID);

        Page<OrganizationViewModel> organizationViewModels = new PageImpl<>(Arrays.asList(organization));
        when(this.organizationService.findAll(this.captor.capture())).thenReturn(organizationViewModels);

        RegisterOrganizationModel registerOrganizationModel = new RegisterOrganizationModel();
        registerOrganizationModel.setId(ORGANIZATIONS_ID);
        registerOrganizationModel.setName(ORGANIZATIONS_NAME);
        registerOrganizationModel.setCountryName(ORGANIZATIONS_COUNTRY_NAME);
        registerOrganizationModel.setUsername(ORGANIZATIONS_USERNAME);
        registerOrganizationModel.setWebsite(ORGANIZATIONS_WEBSITE);
        registerOrganizationModel.setLogo(ORGANIZATIONS_ADDRESS);
        when(this.organizationService.findOne(ORGANIZATIONS_ID)).thenReturn(registerOrganizationModel);
        when(this.organizationService.findOne(INVALID_ORGANIZATIONS_ID)).thenThrow(OrganizationNotFoundExeption.class);


    }

    @Test
    public void showOrganizationsPage_ShouldReturnValidStatusAndPage() throws Exception {
        this.mockMvc.perform(get(ORGANIZATIONS_URL).with(CsrfRequestPostProcessor.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(EXPECTED_URL));

    }

    @Test
    public void verifyOrganizationsServiceIsCalled_ShouldReturnValidCall() throws Exception {
        //Act
        this.mockMvc.perform(get(ORGANIZATIONS_URL));
        //Assert service call
        verify(this.organizationService, times(EXPECTED_CALL)).findAll(captor.capture());
    }

    @Test
    public void verifyOrganizationsPageReturnValidModelToView_ShouldReturnValidOrganizationViewModel() throws Exception {
        this.mockMvc.perform(get(ORGANIZATIONS_URL).with(CsrfRequestPostProcessor.csrf()))
                .andExpect(model().attribute(ORGANIZATIONS_ATTRIBUTE, hasItem(
                        allOf(
                                hasProperty(ID, is(ORGANIZATIONS_ID)),
                                hasProperty(Name, is(ORGANIZATIONS_NAME)),
                                hasProperty(USERNAME, is(ORGANIZATIONS_USERNAME)),
                                hasProperty(LOGO, is(ORGANIZATIONS_ADDRESS)),
                                hasProperty(WEBSITE, is(ORGANIZATIONS_WEBSITE))
                        ))));

    }

    @Test
    public void showSingleOrganizationPage_ShouldReturnValidResponseAndView() throws Exception {
        mockMvc.perform(
                get(SINGLE_ORGANIZATION_URL + ORGANIZATIONS_ID)
                        .with(CsrfRequestPostProcessor.csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name(EXPECTED_SINGLE_ORGANIZATION_URL));
    }

    @Test
    public void verifySingleVolunteerPageModelAttribute_ShouldReturnValidModelAttribute() throws Exception {
        this.mockMvc.perform(
                get(SINGLE_ORGANIZATION_URL + ORGANIZATIONS_ID)
                        .with(CsrfRequestPostProcessor.csrf()))
                .andExpect(model().attribute(REGISTER_MODEL, hasProperty(ID, is(ORGANIZATIONS_ID))))
                .andExpect(model().attribute(REGISTER_MODEL, hasProperty(USERNAME, is(ORGANIZATIONS_USERNAME))))
                .andExpect(model().attribute(REGISTER_MODEL, hasProperty(Name, is(ORGANIZATIONS_NAME))))
                .andExpect(model().attribute(REGISTER_MODEL, hasProperty(LOGO, is(ORGANIZATIONS_ADDRESS))))
                .andExpect(model().attribute(REGISTER_MODEL, hasProperty(WEBSITE, is(ORGANIZATIONS_WEBSITE))));

    }

    @Test
    public void showOrganizationPageWhenGivenWrongId_ShouldReturnErrorPage() throws Exception {
        //Act
        this.mockMvc.perform(get(SINGLE_ORGANIZATION_URL + INVALID_ORGANIZATIONS_ID))
                .andExpect(view().name(EXPECTED_ERROR_PAGE));

    }

    @Test
    public void verifyRegisterOrganizationWhenGivenValidParams_ShouldReturnValidStatusAndView() throws Exception {

        this.mockMvc.perform(post(REGISTER_URL)
                .param(USERNAME, ORGANIZATIONS_USERNAME)
                .param(PASSWORD, EXPECTED_PASSWORD)
                .param(NAME, ORGANIZATIONS_NAME)
                .param(ADDRESS, ORGANIZATIONS_ADDRESS)
                .param(WEBSITE, ORGANIZATIONS_WEBSITE)
                .param(COUNTRY_NAME, ORGANIZATIONS_COUNTRY_NAME)
                .with(CsrfRequestPostProcessor.csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT + REDIRECTED_URL))
                .andExpect(redirectedUrl(REDIRECTED_URL))
                .andExpect(model().hasNoErrors());

        ArgumentCaptor<RegisterOrganizationModel> captor = ArgumentCaptor.forClass(RegisterOrganizationModel.class);
        verify(organizationService).register(captor.capture());
        RegisterOrganizationModel organizationModel = captor.getValue();
        assertEquals(ORGANIZATIONS_USERNAME, organizationModel.getUsername());

    }


    @Test
    public void verifyRegisterOrganizationWhenGivenInValidParams_ShouldReturnToRegisterPage() throws Exception {

        this.mockMvc.perform(post(REGISTER_URL)
                .param(USERNAME, ORGANIZATIONS_USERNAME)
                .param(PASSWORD, EXPECTED_PASSWORD)
                .param(LOGO, ORGANIZATIONS_LOGO)
                .with(CsrfRequestPostProcessor.csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT + REGISTER_ERROR_URL))
                .andExpect(redirectedUrl(REGISTER_ERROR_URL));
    }


}