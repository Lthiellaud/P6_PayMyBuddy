package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.BankMovement;
import com.paymybuddy.webapp.model.Rib;

import java.util.List;
import java.util.Optional;

public interface BankMovementService {

    List<BankMovement> getMovements(List<Rib> ribs);
    BankMovement createMovement(BankMovement bankMovement);

}
