package com.paymybuddy.webapp.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserUtilTest {

    private static User loginUser;



    @Test
    void testToString() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
        loginUser = new User("user@mail.com", "test", grantedAuthorities);

        String userInfo = UserUtil.toString(loginUser);

        assertThat(userInfo).isIn("UserName:user@mail.com (USER, ADMIN)", "UserName:user@mail.com (ADMIN, USER)");

    }
}