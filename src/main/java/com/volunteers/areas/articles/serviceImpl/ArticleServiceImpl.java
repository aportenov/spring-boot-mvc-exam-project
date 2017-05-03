package com.volunteers.areas.articles.serviceImpl;

import com.volunteers.areas.articles.entities.Article;
import com.volunteers.areas.articles.models.binding.RegisterArticleModel;
import com.volunteers.areas.articles.models.view.ArticleViewModel;
import com.volunteers.areas.articles.repositories.ArticleRepository;
import com.volunteers.areas.articles.services.ArticleService;
import com.volunteers.errors.ArticleNotFoundExeption;
import com.volunteers.errors.Errors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articlerepository, ModelMapper modelMapper) {
        this.articleRepository = articlerepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<ArticleViewModel> findAll(Pageable pageable) {
        Page<Article> articles = this.articleRepository.findAll(pageable);
        List<ArticleViewModel> articleViewModels = new ArrayList<>();
        for (Article article : articles) {
            ArticleViewModel articleViewModel = this.modelMapper.map(article, ArticleViewModel.class);
            articleViewModels.add(articleViewModel);
        }

        return new PageImpl<>(articleViewModels,pageable, articles.getTotalElements());
    }

    @Override
    public ArticleViewModel findById(long articleId) {
        Article article = this.articleRepository.findOne(articleId);
        if (article == null){
            throw new ArticleNotFoundExeption(Errors.ARTICLE_NOT_FOUND);
        }

        ArticleViewModel articleViewModel = this.modelMapper.map(article,ArticleViewModel.class);

        return articleViewModel;
    }

    @Override
    public void create(RegisterArticleModel registerArticleModel) {
        Article article = this.modelMapper.map(registerArticleModel, Article.class);
        article.setCreationDate(new Date());
        this.articleRepository.save(article);
    }

    @Override
    public void deleteArticle(long articleId) {
        Article article = this.articleRepository.findOne(articleId);
        if (article == null) {
            throw new ArticleNotFoundExeption(Errors.ARTICLE_NOT_FOUND);
        }

        this.articleRepository.delete(article);
    }
}
