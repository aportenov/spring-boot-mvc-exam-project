package com.volunteers.areas.articles.services;


import com.volunteers.areas.articles.models.view.ArticleViewModel;
import com.volunteers.areas.articles.models.binding.RegisterArticleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    Page<ArticleViewModel> findAll(Pageable pageable);

    ArticleViewModel findById(long articleId);

    void create(RegisterArticleModel registerArticleModel);

    void deleteArticle(long articleId);
}




