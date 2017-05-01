package com.volunteers.config;

import com.volunteers.areas.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private  UserService userService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/images/**", "/bootstrap/**", "/js/**","/css/**", "/plugins/**","/fonts/**").permitAll()
                .antMatchers("/","/countries","/login","/register/**","/connect/**","/twitter/**", "/facebook/**").permitAll()
                .anyRequest().authenticated()
             .and()
                .formLogin().loginPage("/login").permitAll()
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .rememberMe()
                .rememberMeCookieName("RememberMe")
                .rememberMeParameter("rememberMe")
                .key("VolunteerDatabase")
                .tokenValiditySeconds(1000)
                .and()
                .logout().logoutSuccessUrl("/").logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
                .deleteCookies("JSESSIONID")
                .and()
                .exceptionHandling().accessDeniedPage("/error")
                .and()
        .csrf().csrfTokenRepository(csrfTokenRepository()).ignoringAntMatchers("/connect/**");

    }

    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }

}
