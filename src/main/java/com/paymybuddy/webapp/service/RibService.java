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
public interface RibService {

    /**
     * gives the list of rib of a user
     * @param user the given user
     * @return the list of founded rib
     */
    List<BankAccount> getRibsByUser(PMBUser user);

    /**
     * gives a rib from an id
     * @param ribId the given id
     * @return the rib if founded
     */
    Optional<BankAccount> getById(Long ribId);

    /**
     * creates a new bankAccount
     * @param bankAccount the new bankAccount to be created
     * @return the created bankAccount
     */
    BankAccount createRib(BankAccount bankAccount);

    /**
     * gives a RIB from a user and a rib name
     * @param ribName the given rib name
     * @param user the given user
     * @return the rib if founded
     */
    Optional<BankAccount> getByRibNameAndUser(String ribName, PMBUser user);

    /**
     * gives a RIB from a user, iban and bic
     * @param iban the given iban
     * @param bic the given BIC
     * @param user the given user
     * @return the rib if founded
     */
    Optional<BankAccount> getByIbanAndBicAndUser(String iban, String bic, PMBUser user);

    /**
     * Verifies data and register a new rib
     * @param bankAccountDTO data entered by the user
     * @return response of the operation
     */
    Response processRib(BankAccountDTO bankAccountDTO);
}
