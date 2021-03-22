package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.PMBUser;

public interface PMBUserService {

   PMBUser getByEmail(String email);
   PMBUser getCurrentUser();
}
