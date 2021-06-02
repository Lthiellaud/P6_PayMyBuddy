package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.BankMovement;
import com.paymybuddy.webapp.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * gives access to BankMovement records.
 */
@Repository
public interface BankMovementRepository extends JpaRepository<BankMovement, Long> {
    /**
     * To retrieve a list of bank movements from a list of RIB.
     * @param bankAccounts The given list of RIB
     * @return The list of bank movements
     */
    List<BankMovement> findAllByBankAccountInOrderByMovementDateDesc(List<BankAccount> bankAccounts);

}
