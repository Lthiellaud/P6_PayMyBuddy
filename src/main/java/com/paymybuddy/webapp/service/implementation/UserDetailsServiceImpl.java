package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.service.PMBUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PMBUserService pmbUserService;

    //TODO add log - remove sysout

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        Optional<PMBUser> user = pmbUserService.getByEmail(email);
        if (!user.isPresent()) {
            System.out.println("User not found! " + email);
            throw new UsernameNotFoundException("User with email " + email + " not found in the database");
        }
        PMBUser pmbUser = user.get();
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        return buildUserForAuthentication(pmbUser, grantedAuthorities);
    }

    private UserDetails buildUserForAuthentication(PMBUser PMBUser, Set<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(PMBUser.getEmail(), PMBUser.getPassword(),
                true, true, true, true, authorities);
    }
}
