package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.repository.PMBUserRepository;
import com.paymybuddy.webapp.service.PMBUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class PMBUserServiceImpl implements PMBUserService {

    @Autowired
    private PMBUserRepository PMBUserRepository;


    @Override
    public PMBUser getByEmail(String email) {
        return PMBUserRepository.findUserByEmail(email);
    }

    @Override
    public PMBUser getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails)principal).getUsername();
            return getByEmail(username);
        } else {
            return null;
        }

    }
}
