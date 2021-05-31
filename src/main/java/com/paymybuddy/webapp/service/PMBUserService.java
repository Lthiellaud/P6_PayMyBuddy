package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.PMBUser;

import java.util.Optional;

public interface PMBUserService {

   Optional<PMBUser> getByEmail(String email);
   PMBUser getCurrentUser();
   String getWelcomeMessage(PMBUser user);
   String getBalanceMessage(PMBUser user);

   PMBUser saveUser(PMBUser user);
   PMBUser updateUserBalance(PMBUser user, double amount);

}
