package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.service.PMBUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PMBUserService pmbUserService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        PMBUser PMBUser = pmbUserService.getByEmail(email);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        return buildUserForAuthentication(PMBUser, grantedAuthorities);
    }

    private UserDetails buildUserForAuthentication(PMBUser PMBUser, Set<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(PMBUser.getEmail(), PMBUser.getPassword(),
                true, true, true, true, authorities);
    }
}
