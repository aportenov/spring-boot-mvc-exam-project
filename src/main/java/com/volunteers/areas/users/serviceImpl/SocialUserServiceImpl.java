package com.volunteers.areas.users.serviceImpl;

import com.volunteers.areas.users.services.SocialUserService;
import com.volunteers.config.Params;
import com.volunteers.areas.countries.entities.Country;
import com.volunteers.areas.users.entities.SocialUser;
import com.volunteers.enumerators.Provider;
import com.volunteers.areas.countries.repositories.CountryRepository;
import com.volunteers.areas.users.repositories.SocialUserRepository;
import com.volunteers.areas.users.services.RoleService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.facebook.api.User;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class SocialUserServiceImpl implements SocialUserService {

    private final SocialUserRepository socialUserRepository;
    private final RoleService roleService;
    private final CountryRepository countryRepository;

    public SocialUserServiceImpl(SocialUserRepository socialUserRepository, RoleService roleService,
                                 CountryRepository countryRepository) {
        this.socialUserRepository = socialUserRepository;
        this.roleService = roleService;
        this.countryRepository = countryRepository;
    }

    @Override
    public void proceedFbUser(User facebookUser, String image) {
        String email = facebookUser.getEmail();
        SocialUser socialUser = this.socialUserRepository.findOneByUsername(email);
        if (socialUser == null) {
            socialUser = registerFbUser(facebookUser, image);
        }

        loginUser(socialUser);
    }

    @Override
    public void proceedTwitterUser(TwitterProfile twitterUser) {
        String username = twitterUser.getScreenName();
        SocialUser socialUser = this.socialUserRepository.findOneByUsername(username);
        if (socialUser == null) {
            socialUser = registerTwitterUser(twitterUser);
        }

        loginUser(socialUser);
    }

    private SocialUser registerTwitterUser(TwitterProfile twitterUser) {
        SocialUser user = new SocialUser();
        user.setUsername(twitterUser.getScreenName());
        user.setName(twitterUser.getName());
        user.setImage(twitterUser.getProfileImageUrl());
        Country country = this.countryRepository.findOneByCountryName(twitterUser.getLocation());
        user.setCountry(country);
        user.setProvider(Provider.TWITTER);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.addRole(this.roleService.getSocialRole());
        this.socialUserRepository.save(user);

        return user;
    }

    @Transactional
    private SocialUser registerFbUser(User facebookUser, String image) {
        SocialUser user = new SocialUser();
        user.setUsername(facebookUser.getEmail());
        String userCountry = facebookUser.getLocation().getName().split("[\\s,]+")[Params.COUNTRY_NAME];
        Country country = this.countryRepository.findOneByCountryName(userCountry);
        user.setCountry(country);
        user.setProvider(Provider.FACEBOOK);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setName(facebookUser.getName());
        user.setImage(image);
        user.addRole(this.roleService.getSocialRole());
        this.socialUserRepository.save(user);

        return user;
    }

    private void loginUser(SocialUser socialUser) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(socialUser, null, socialUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
}