package com.volunteers.areas.articles.controllers;

import com.volunteers.areas.articles.models.binding.RegisterArticleModel;
import com.volunteers.areas.articles.models.view.ArticleViewModel;
import com.volunteers.areas.articles.services.ArticleService;
import com.volunteers.config.CsrfRequestPostProcessor;
import com.volunteers.errors.ArticleNotFoundExeption;
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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ArticleController.class)
@EnableSpringDataWebSupport
@ActiveProfiles("test")
public class ArticleControllerTest {

    private static final String CREATE_ARTICLE_URL = "/article/add";
    private static final String MODEL_ATTRIBUTE = "articles";
    private static final long ARTICLE_ID = 1;
    private static final long INVALID_ARTICLE_ID = -1;
    private static final String ARTICLE_NAME = "article";
    private static final String ARTICLE_CONTENT = "12345678911";
    private static final String ARTICLES_ULR = "/articles/all";
    private static final String ID = "id";
    private static final String Name = "name";
    private static final String TEXT = "text";
    private static final String ARTICLE_TEXT = "text text text text ";
    private static final String CONTENT = "content";
    private static final String EDIT_ARTICLE_URL = "/article/edit/";
    private static final String ARTICLE_MODEL = "article";
    private static final int EXPECTED_CALL = 1 ;
    private static final String REGISTER_URL = "/article/register";
    private static final String REDIRECT = "redirect:";
    private static final String REDIRECTED_URL = "/articles/all";
    private static final String EXPECTED_ERROR_PAGE = "/error";


    @MockBean
    private ArticleService articleService;

    @Captor
    private ArgumentCaptor<Pageable> captor;

    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();

        ArticleViewModel articleViewModel = new ArticleViewModel();
        articleViewModel.setId(ARTICLE_ID);
        articleViewModel.setName(ARTICLE_NAME);
        articleViewModel.setContent(ARTICLE_CONTENT);
        Page<ArticleViewModel> articleViewModels = new PageImpl<>(Arrays.asList(articleViewModel));
        when(this.articleService.findAll(this.captor.capture())).thenReturn(articleViewModels);
        when(this.articleService.findById(ARTICLE_ID)).thenReturn(articleViewModel);
        when(this.articleService.findById(INVALID_ARTICLE_ID)).thenThrow(ArticleNotFoundExeption.class);

    }

    @Test
    public void showCreateArticlePage_ShouldReturnCorretPage() throws Exception {
        this.mockMvc.perform(get(CREATE_ARTICLE_URL)
                .with(CsrfRequestPostProcessor.csrf()))
                .andExpect(status().isOk());

    }

    @Test
    public void showArticlesPage_ShouldReturnValidModels() throws Exception {
        this.mockMvc.perform(get(ARTICLES_ULR).with(CsrfRequestPostProcessor.csrf()))
                .andExpect(model().attribute(MODEL_ATTRIBUTE, hasItem(
                        allOf(
                                hasProperty(ID, is(ARTICLE_ID)),
                                hasProperty(Name, is(ARTICLE_NAME)),
                                hasProperty(CONTENT, is(ARTICLE_CONTENT))

                        ))));

        verify(this.articleService, times(EXPECTED_CALL)).findAll(captor.capture());

    }

    @Test
    public void showEditArticlePage_ShouldReturnCorrectModelAndPage() throws Exception {
        this.mockMvc.perform(get(EDIT_ARTICLE_URL + ARTICLE_ID)
                .with(CsrfRequestPostProcessor.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ARTICLE_MODEL, hasProperty(ID, is(ARTICLE_ID))))
                .andExpect(model().attribute(ARTICLE_MODEL, hasProperty(Name, is(ARTICLE_NAME))))
                .andExpect(model().attribute(ARTICLE_MODEL, hasProperty(CONTENT, is(ARTICLE_CONTENT))));
    }

    @Test
    public void createArticle_ShouldCreateArticle() throws Exception {
        this.mockMvc.perform(post(REGISTER_URL)
                .param(Name, ARTICLE_NAME)
                .param(CONTENT, ARTICLE_CONTENT)
                .param(TEXT, ARTICLE_TEXT)
                .with(CsrfRequestPostProcessor.csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT + REDIRECTED_URL))
                .andExpect(redirectedUrl(REDIRECTED_URL))
                .andExpect(model().hasNoErrors());

        ArgumentCaptor<RegisterArticleModel> captor = ArgumentCaptor.forClass(RegisterArticleModel.class);
        verify(articleService).create(captor.capture());
        RegisterArticleModel registerArticleModel = captor.getValue();
        assertEquals(ARTICLE_NAME, registerArticleModel.getName());

    }

    @Test
    public void createArticleModelWithInvalidId_ShouldThrowExeption() throws Exception {
        this.mockMvc.perform(get(EDIT_ARTICLE_URL + INVALID_ARTICLE_ID)
                .with(CsrfRequestPostProcessor.csrf()))
                .andExpect(view().name(EXPECTED_ERROR_PAGE));

    }

}