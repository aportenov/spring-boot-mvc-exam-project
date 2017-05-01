package com.volunteers.areas.users.serviceImpl;

import com.volunteers.areas.countries.entities.Country;
import com.volunteers.areas.countries.repositories.CountryRepository;
import com.volunteers.areas.users.entities.Role;
import com.volunteers.areas.users.entities.SocialUser;
import com.volunteers.areas.users.repositories.SocialUserRepository;
import com.volunteers.areas.users.services.RoleService;
import com.volunteers.areas.users.services.SocialUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.social.facebook.api.Reference;
import org.springframework.social.facebook.api.User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest

@ActiveProfiles("test")
public class SocialUserServiceImplTest {

    private static final String EXPECTED_EMAIL = "valid@test.com";
    private static int EXPECTED_REPOSITORY_CALL = 1;
    private static String COUNTRY_NAME = "Country";
    private static String FACEBOOK_IMAGE = "base64image";
    private static String EXPECTED_LOCATION = "EXPECTED LOCATION";
    private static int REFERENCE_ID = 1;


    @MockBean
    private SocialUserRepository socialUserRepository;

    @MockBean
    private RoleService roleService;

    @MockBean
    private CountryRepository countryRepository;

    @Autowired
    private SocialUserService socialUserService;

    @Captor
    public  ArgumentCaptor<SocialUser> captor;

    @Mock
    private User user ;

    @Before
    public void setUp() throws Exception {
        Role role = new Role();
        role.setAuthority("ROLE_SOCIAL");
        Country country = new Country();
        when(this.roleService.getSocialRole()).thenReturn(role);
        when(this.user.getLocation()).thenReturn(new Reference(EXPECTED_LOCATION, EXPECTED_LOCATION));
        when(this.countryRepository.findOneByCountryName(COUNTRY_NAME)).thenReturn(country);
        when(this.user.getEmail()).thenReturn(EXPECTED_EMAIL);
        when(this.socialUserRepository.findOneByUsername(EXPECTED_EMAIL)).thenReturn(null);


    }

    @Test
    public void verifyValidFbUserGivenIsPassedToRepository_ShouldReturnValidRepositoryCall() throws Exception {

        //Act
        this.socialUserService.proceedFbUser(this.user, FACEBOOK_IMAGE);

        //Assert database call
        verify(this.socialUserRepository , times(EXPECTED_REPOSITORY_CALL)).save(captor.capture());
    }

    @Test
    public void verifyValidFbUserGivenEmailIsSavedToSocialUser_ShouldReturnValidEmail() throws Exception {

        //Act
        this.socialUserService.proceedFbUser(this.user, FACEBOOK_IMAGE);

        //Assert email
        verify(socialUserRepository).save(this.captor.capture());
        SocialUser socialUser = captor.getValue();
        assertThat(socialUser.getUsername(), is(EXPECTED_EMAIL) );
    }

}