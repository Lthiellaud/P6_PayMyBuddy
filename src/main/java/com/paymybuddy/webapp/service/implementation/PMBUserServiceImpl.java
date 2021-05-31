package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.repository.PMBUserRepository;
import com.paymybuddy.webapp.service.PMBUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PMBUserServiceImpl implements PMBUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PMBUserServiceImpl.class);

    @Autowired
    private PMBUserRepository pmbUserRepository;

    @Override
    public Optional<PMBUser> getByEmail(String email) {
        return pmbUserRepository.findUserByEmail(email);
    }

    @Override
    public PMBUser getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserDetails)) {
            return null;
        }
        String username = ((UserDetails)principal).getUsername();
        Optional<PMBUser> pmbUser = getByEmail(username);
        return pmbUser.orElse(null);
    }

    @Override
    public String getWelcomeMessage(PMBUser user) {
        return "Hello " + user.getFirstName() + " " + user.getLastName();
    }

    @Override
    public String getBalanceMessage(PMBUser user) {
        return "You have <strong>" + user.getBalance() + "</strong> € on your account";
    }

    @Override
    public PMBUser saveUser(PMBUser user) {
        return pmbUserRepository.save(user);
    }

    @Override
    public PMBUser updateUserBalance(PMBUser user, double amount) {
        user.setBalance(user.getBalance() + amount);
        LOGGER.debug("updateUserBalance for userID " + user.getUserId()
                + " - new balance = " + user.getBalance());

        return saveUser(user);

    }

}
