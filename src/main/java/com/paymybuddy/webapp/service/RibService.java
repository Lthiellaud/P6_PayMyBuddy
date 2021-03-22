package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Rib;

import java.util.List;
import java.util.Optional;

public interface RibService {

    List<Rib> getRibs();
    Optional<Rib> createRib();

}
