package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.BankMovement;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.repository.BankMovementRepository;
import com.paymybuddy.webapp.service.BankMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankMovementServiceImpl implements BankMovementService {
    @Autowired
    private BankMovementRepository bankMovementRepository;

    @Override
    public List<BankMovement> getMovements(List<BankAccount> bankAccounts) {
        return bankMovementRepository.findAllByBankAccountInOrderByMovementDateDesc(bankAccounts);
    }

    @Override
    public BankMovement createMovement(BankMovement bankMovement) {
        return bankMovementRepository.save(bankMovement);
    }

}
