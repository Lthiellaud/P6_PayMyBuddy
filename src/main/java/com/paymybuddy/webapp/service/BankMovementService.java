package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.BankMovement;

import java.util.List;
import java.util.Optional;

public interface BankMovementService {

    List<BankMovement> getMovements();
    Optional<BankMovement> createMovement();

}
