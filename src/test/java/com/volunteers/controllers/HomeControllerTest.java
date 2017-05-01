package com.volunteers.controllers;

import com.volunteers.areas.articles.models.view.ArticleViewModel;
import com.volunteers.areas.articles.services.ArticleService;
import com.volunteers.config.CsrfRequestPostProcessor;
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

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
@EnableSpringDataWebSupport
@ActiveProfiles("test")
public class HomeControllerTest {

    private static final String GET_URL = "/";
    private static final String EXPECTED_VIEW_NAME = "home";
    private static final String ARTICLE_NAME = "Article Name";
    private static final String EXPECTED_USERNAME = "username@aaa.bg";
    private static final String EXPECTED_PASSWORD = "Mypassword1";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String CONFIRMED_PASSWORD = "confirmPassword";
    private static final String REGISTER_URL = "/register";
    private static final String REDIRECT = "redirect:/";
    private static final String HOME_URL = "/";

    @MockBean
    private ArticleService articleService;

    @Captor
    private ArgumentCaptor<Pageable> captor;


    private HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();

        ArticleViewModel articleViewModel = new ArticleViewModel();
        articleViewModel.setName(ARTICLE_NAME);
        Page<ArticleViewModel> articleViewModels = new PageImpl<>(Arrays.asList(articleViewModel));
        when(this.articleService.findAll(this.captor.capture())).thenReturn(articleViewModels);

    }

    @Test
    public void showHomePage_ShouldReturnValidStatusAndHomePage() throws Exception {
        //Act
        this.mockMvc.perform(get(GET_URL)
                .with(CsrfRequestPostProcessor.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(EXPECTED_VIEW_NAME));
    }

    @Test
    public void showArticlesOnHomePage_ShouldReturnValidArticleList() throws Exception {
        //Act
        this.mockMvc.perform(get(GET_URL).with(CsrfRequestPostProcessor.csrf()))
                .andExpect(status().isOk());

    }

}
