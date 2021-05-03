package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.BankMovement;
import com.paymybuddy.webapp.model.Rib;
import com.paymybuddy.webapp.repository.BankMovementRepository;
import com.paymybuddy.webapp.service.BankMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankMovementServiceImpl implements BankMovementService {
    @Autowired
    private BankMovementRepository bankMovementRepository;

    @Override
    public List<BankMovement> getMovements(List<Rib> ribs) {
        List<BankMovement> movements = bankMovementRepository.findAllByRibInOrderByMovementDateDesc(ribs);
        return movements;
    }

    @Override
    public BankMovement createMovement(BankMovement bankMovement) {
        return bankMovementRepository.save(bankMovement);
    }

}
