package com.volunteers.areas.articles.serviceImpl;

import com.volunteers.areas.articles.entities.Article;
import com.volunteers.areas.articles.models.view.ArticleViewModel;
import com.volunteers.areas.articles.repositories.ArticleRepository;
import com.volunteers.areas.articles.services.ArticleService;
import com.volunteers.errors.ArticleNotFoundExeption;
import com.volunteers.errors.Errors;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ArticleServiceImplTest {

    private static final int EXPECTED_REPOSITORY_CALL = 1;
    private static final long VALID_ID = 1;
    private static final long INVALID_ID = -1;
    private static final String VALID_NAME = "TEST";
    private static final String EXPECTED_ERROR_MSG = "Article not found";

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ArticleService articleService;

    @MockBean
    private ArticleRepository articleRepository;

    @Before
    public void SetUp() throws Exception {
        Article article = new Article();
        article.setId(VALID_ID);
        article.setName(VALID_NAME);

        when(this.articleRepository.findOne(VALID_ID)).thenReturn(article);
        when(this.articleRepository.findOne(INVALID_ID)).thenThrow(new ArticleNotFoundExeption(Errors.ARTICLE_NOT_FOUND));


    }

    @Test
    public void findByIdGivenValidArticle_ShouldReturnValidModelId() throws Exception {
        //Act
        ArticleViewModel articleViewModel = this.articleService.findById(VALID_ID);

        //Assert ID
        assertEquals(VALID_ID,(long) articleViewModel.getId());
    }


    @Test
    public void findByIdGivenValidArticle_ShouldReturnValidModelName() throws Exception {
        //Act
        ArticleViewModel articleViewModel = this.articleService.findById(VALID_ID);

        //Assert Name
        assertEquals(VALID_NAME, articleViewModel.getName());
    }


    @Test
    public void findByIdGivenValidArticle_ShouldCallRepository() throws Exception {
        //Act
        this.articleService.findById(VALID_ID);

        //Assert Database Call
        verify(this.articleRepository,times(EXPECTED_REPOSITORY_CALL)).findOne(VALID_ID);
    }


    @Test(expected = ArticleNotFoundExeption.class)
    public void findByIdGivenInvalidId_ShouldThrowExeption() throws Exception {
        //Act
        this.articleService.findById(INVALID_ID);

        //Assert exeptpion
        expectedEx.expect(ArticleNotFoundExeption.class);




    }

    @Test(expected = ArticleNotFoundExeption.class)
    public void findByIdGivenInvalidId_ShouldThrowValidExeptionMsg() throws Exception {
        //Act
        this.articleService.findById(INVALID_ID);
        //Assert exeptpion msg
        expectedEx.expectMessage(EXPECTED_ERROR_MSG);




    }


}