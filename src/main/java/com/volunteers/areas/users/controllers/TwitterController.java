package com.volunteers.areas.users.controllers;

import com.volunteers.areas.users.services.SocialUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/twitter")
public class TwitterController {

    private final Twitter twitter;
    private final ConnectionRepository connectionRepository;

    @Autowired
    private SocialUserService socialUserService;

    public TwitterController(Twitter twitter, ConnectionRepository connectionRepository) {
        this.twitter = twitter;
        this.connectionRepository = connectionRepository;
    }

    @GetMapping("connected")
    public String home(Principal currentUser, Model model) {
        Connection<Twitter> connection = connectionRepository.findPrimaryConnection(Twitter.class);
        if (connection == null) {
            return "redirect:/connect/twitter";
        }

        TwitterProfile user = connection.getApi().userOperations().getUserProfile();
        this.socialUserService.proceedTwitterUser(user);

        return "redirect:/";
    }

}