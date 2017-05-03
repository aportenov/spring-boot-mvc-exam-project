package com.volunteers.areas.articles.controllers;

import com.volunteers.areas.articles.services.ArticleService;
import com.volunteers.areas.articles.models.view.ArticleViewModel;
import com.volunteers.areas.articles.models.binding.RegisterArticleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/article/add")
    public String showCreateArticlePage(Model model) {
        if (!model.containsAttribute("article")) {
            model.addAttribute("article", new RegisterArticleModel());
        }

        model.addAttribute("title" , "Create Article");

        return "/articles/add-article";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/articles/all")
    public String showArticlesPage(Model model
            ,@PageableDefault(size = 5) Pageable pageable) {

        Page<ArticleViewModel> articleViewModels = this.articleService.findAll(pageable);
        model.addAttribute("articles", articleViewModels);
        model.addAttribute("title" , "Articles");

        return "/articles/show-articles";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/article/edit/{articleId}")
    public String showEditArticlePage(@PathVariable("articleId")long articleId, Model model) {

        if (!model.containsAttribute("article")) {
            ArticleViewModel articleViewModel = this.articleService.findById(articleId);
            model.addAttribute("article", articleViewModel);
        }

        model.addAttribute("title" , "Articles");

        return "/articles/edit-article";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/article/update/{articleId}")
    public String updateArticle(@PathVariable("articleId")long articleId, @Valid
                                @ModelAttribute RegisterArticleModel registerArticleModel,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.article", bindingResult);
            redirectAttributes.addFlashAttribute("article", registerArticleModel);

            return "redirect:/articles/edit-article";

        }

        this.articleService.create(registerArticleModel);

        return "redirect:/articles/all";

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/article/register")
    public String addArticle(@Valid @ModelAttribute RegisterArticleModel registerArticleModel,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.article", bindingResult);
            redirectAttributes.addFlashAttribute("article", registerArticleModel);

            return "redirect:/article/add";

        }

        this.articleService.create(registerArticleModel);

        return "redirect:/articles/all";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/article/delete/{articleId}")
    public String showDeleteArticlePage(@PathVariable("articleId")long articleId, Model model) {

        if (!model.containsAttribute("article")) {
            ArticleViewModel articleViewModel = this.articleService.findById(articleId);
            model.addAttribute("article", articleViewModel);
        }


        return "/articles/delete-article";
    }

    @PostMapping("/article/delete/{articleId}")
    public String deleteArticle(@PathVariable("articleId")long articleId, Model model) {
        this.articleService.deleteArticle(articleId);
        return "/articles/delete-article";
    }

}
