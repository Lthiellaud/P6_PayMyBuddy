package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.DTO.BankAccountDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.constants.Response;

import java.util.List;
import java.util.Optional;

/**
 * Service used by BankAccount controller and to manage BankAccount data
 */
public interface BankAccountService {

    /**
     * gives the list of bank account of a user
     * @param user the given user
     * @return the list of founded bank account
     */
    List<BankAccount> getBankAccountsByUser(PMBUser user);

    /**
     * gives a bank account from an id
     * @param bankAccountId the given id
     * @return the bank account if founded
     */
    Optional<BankAccount> getById(Long bankAccountId);

    /**
     * creates a new bankAccount
     * @param bankAccount the new bankAccount to be created
     * @return the created bankAccount
     */
    BankAccount createBankAccount(BankAccount bankAccount);

    /**
     * gives a BANK ACCOUNT from a user and a bank account name
     * @param bankAccountName the given bank account name
     * @param user the given user
     * @return the bank account if founded
     */
    Optional<BankAccount> getByBankAccountNameAndUser(String bankAccountName, PMBUser user);

    /**
     * gives a BANK ACCOUNT from a user, iban and bic
     * @param iban the given iban
     * @param bic the given BIC
     * @param user the given user
     * @return the bank account if founded
     */
    Optional<BankAccount> getByIbanAndBicAndUser(String iban, String bic, PMBUser user);

    /**
     * Verifies data and register a new bank account
     * @param bankAccountDTO data entered by the user
     * @return response of the operation
     */
    Response processBankAccount(BankAccountDTO bankAccountDTO);
}
