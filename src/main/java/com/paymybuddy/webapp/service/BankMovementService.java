package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.BankMovement;

import java.util.List;

public interface BankMovementService {

    List<BankMovement> getMovements(List<BankAccount> bankAccounts);
    BankMovement createMovement(BankMovement bankMovement);

}
