package com.volunteers.areas.users.entities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class AbstractUserTest {

    public static final String EXPECTED_ROLE_NAME = "ROLE_SOCIAL";

    private SocialUser socialUser;

    @Mock
    private Role role;

    @Before
    public void SetUp() throws Exception {
        this.socialUser = new SocialUser();
        when(this.role.getAuthority()).thenReturn("ROLE_SOCIAL");
    }

    @Test
    public void addRoleWhenSocialUserRoleGiven_ShouldReturnCorrectRoleName() throws Exception {
        this.socialUser.addRole(this.role);
        String actualRoleName =  this.socialUser.getAuthorities().iterator().next().getAuthority();

        assertEquals(EXPECTED_ROLE_NAME, actualRoleName );

    }

}