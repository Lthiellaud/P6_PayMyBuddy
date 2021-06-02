package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.PMBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * gives access to BankAccount records.
 */
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    /**
     * To retrieve a list of Bank Account from a PMB User.
     * @param user the PMB user
     * @return The list of Bank Account
     */
    List<BankAccount> findAllByUser(PMBUser user);

    /**
     * To retrieve a Bank Account from a Bank Account name and a PMB User.
     * @param bankAccountName the Bank Account name
     * @param user the PMB user
     * @return The Bank Account if founded
     */
    Optional<BankAccount> findByBankAccountNameAndUser(String bankAccountName, PMBUser user);

    /**
     * To retrieve a Bank Account from a IBAN and a BIC.
     * @param iban the IBAN
     * @param bic the BIC
     * @param user the PMB user
     * @return the Bank Account if founded
     */
    Optional<BankAccount> findByIbanAndBicAndUser(String iban, String bic, PMBUser user);
}
