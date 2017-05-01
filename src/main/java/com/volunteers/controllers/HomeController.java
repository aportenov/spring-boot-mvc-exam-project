package com.volunteers.controllers;

import com.volunteers.areas.articles.models.view.ArticleViewModel;
import com.volunteers.areas.users.models.binding.RegistrationModel;
import com.volunteers.areas.articles.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    private final ArticleService articleService;

    @Autowired
    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String getHomePage(Model model,@PageableDefault(size = 5) Pageable pageable){

        Page<ArticleViewModel> articleViewModelt = this.articleService.findAll(pageable);

        if (! model.containsAttribute("registrationModel")) {
            model.addAttribute("registrationModel", new RegistrationModel());
        }

        model.addAttribute("articles", articleViewModelt);
        model.addAttribute("title", "Volunteer Network");

        return "home";
    }
}
