package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.DTO.AccountDTO;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.constants.Response;

/**
 * Service used by Account controller
 */
public interface AccountService {
    /**
     * Verifies data and calls the registration to process an account operation.
     * @param accountDTO data entered by the user
     * @return response of the operation
     */
    Response operateMovement(AccountDTO accountDTO);

    /**
     * Registers the new account movement, updates user PMB account and sends a bank transaction.
     * (transactional)
     * @param accountDTO data entered by the user
     * @param bankAccount The bankAccount to be used for the transaction
     * @return response of the operation
     */
    Response registerMovement(AccountDTO accountDTO, BankAccount bankAccount);
}
