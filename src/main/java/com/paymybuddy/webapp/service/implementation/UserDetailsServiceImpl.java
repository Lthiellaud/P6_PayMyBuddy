package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.controller.TransferController;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.service.PMBUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

    /**
     * To get User details from an email (Grant authority = USER)
     * @param email the given email
     * @return User details
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        Optional<PMBUser> user = pmbUserService.getByEmail(email);
        if (!user.isPresent()) {
            LOGGER.debug("User not found! " + email);
            throw new UsernameNotFoundException("User with email " + email + " not found in the database");
        }
        PMBUser pmbUser = user.get();
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        return buildUserForAuthentication(pmbUser, grantedAuthorities);
    }

    /**
     * Build of the User details
     * @param PMBUser The user
     * @param authorities authorities of the user
     * @return User details
     */
    private UserDetails buildUserForAuthentication(PMBUser PMBUser, Set<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(PMBUser.getEmail(), PMBUser.getPassword(),
                true, true, true, true, authorities);
    }
}
