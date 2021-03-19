package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.User;

public interface UserService {

    User getByUsername(String username);
}
