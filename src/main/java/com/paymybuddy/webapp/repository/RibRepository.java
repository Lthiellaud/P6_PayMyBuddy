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
public interface RibRepository extends JpaRepository<BankAccount, Long> {

    /**
     * To retrieve a list of RIB from a PMB User.
     * @param user the PMB user
     * @return The list of RIB
     */
    List<BankAccount> findAllByUser(PMBUser user);

    /**
     * To retrieve a RIB from a RIB name and a PMB User.
     * @param ribName the RIB name
     * @param user the PMB user
     * @return The RIB if founded
     */
    Optional<BankAccount> findByRibNameAndUser(String ribName, PMBUser user);

    /**
     * To retrieve a RIB from a IBAN and a BIC.
     * @param iban the IBAN
     * @param bic the BIC
     * @param user the PMB user
     * @return the RIB if founded
     */
    Optional<BankAccount> findByIbanAndBicAndUser(String iban, String bic, PMBUser user);
}
