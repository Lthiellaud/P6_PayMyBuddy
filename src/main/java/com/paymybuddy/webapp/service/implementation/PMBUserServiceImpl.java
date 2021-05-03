package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.controller.TransferController;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.repository.PMBUserRepository;
import com.paymybuddy.webapp.service.PMBUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class PMBUserServiceImpl implements PMBUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

    @Autowired
    private PMBUserRepository pmbUserRepository;

    @Override
    public PMBUser getByEmail(String email) {
        return pmbUserRepository.findUserByEmail(email);
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

    @Override
    public String getWelcomeMessage(PMBUser user) {
        String welcome = "Hello " + user.getFirstName() + " " + user.getLastName();
        return welcome;
    }

    @Override
    public String getBalanceMessage(PMBUser user) {
        String balance ="You have <strong>" + user.getBalance() + "</strong> € on your account";
        return balance;
    }

    @Override
    public PMBUser saveUser(PMBUser user) {
        return pmbUserRepository.save(user);
    }

    @Override
    public void updateUserBalance(PMBUser user, double amount) {
        user.setBalance(user.getBalance() + amount);
        LOGGER.debug("updateUserBalance for userID " + user.getUserId()
                + " - new balance = " + user.getBalance());
        saveUser(user);

    }

}
