package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.PMBUser;

public interface UserService {

    PMBUser getByUsername(String username);
}
