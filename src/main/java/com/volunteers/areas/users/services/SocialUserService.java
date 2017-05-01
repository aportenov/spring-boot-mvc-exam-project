package com.volunteers.areas.users.services;


import org.springframework.social.facebook.api.User;
import org.springframework.social.twitter.api.TwitterProfile;

public interface SocialUserService {

    void proceedFbUser(User facebookUser, String image);

    void proceedTwitterUser(TwitterProfile twitterUser);
}
