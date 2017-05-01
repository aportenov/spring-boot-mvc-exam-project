package com.volunteers.areas.users.controllers;

import com.volunteers.areas.users.services.UserService;
import com.volunteers.config.CsrfRequestPostProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@EnableSpringDataWebSupport
@ActiveProfiles("test")
public class UserControllerTest {

    private static final String EXPECTED_USERNAME = "u@aaa.bg";
    private static final String EXPECTED_PASSWORD = "Mypassword1";
    private static final String INVALID_PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String CONFIRMED_PASSWORD = "confirmPassword";
    private static final String REGISTER_URL = "/register";
    private static final String REDIRECT = "redirect:";
    private static final String HOME_URL = "/";

    @MockBean
    private UserService userService;

    private HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
       when(this.userService.loadUserByUsername(EXPECTED_USERNAME)).thenThrow(UsernameNotFoundException.class);

    }

    @Test
    public void registerUser() throws Exception {

        mockMvc
                .perform(post(REGISTER_URL).with(CsrfRequestPostProcessor.csrf())
                        .param(USERNAME, EXPECTED_USERNAME)
                        .param(PASSWORD, EXPECTED_PASSWORD)
                        .param(CONFIRMED_PASSWORD, EXPECTED_PASSWORD)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(REGISTER_URL))
                .andExpect(model().hasNoErrors());

        verify(this.userService).loadUserByUsername(EXPECTED_USERNAME);
    }


    @Test
    public void registerGivenInvalidPasswords_ShouldNotRegister() throws Exception {
        this.mockMvc
                .perform(post(REGISTER_URL)
                        .param(PASSWORD, EXPECTED_PASSWORD)
                        .param(CONFIRMED_PASSWORD, INVALID_PASSWORD)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT + HOME_URL));
    }

}