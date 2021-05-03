package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.Rib;

import java.util.List;
import java.util.Optional;

public interface RibService {

    List<Rib> getRibsByUser(PMBUser user);
    Optional<Rib> getById(Long ribId);
    Optional<Rib> createRib();

}
