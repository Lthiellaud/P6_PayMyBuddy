package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.PMBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * gives access to PMBUser records.
 */
@Repository
public interface PMBUserRepository extends JpaRepository<PMBUser, Long> {

    /**
     * To retrieve a PMB user from an email.
     * @param email The email
     * @return the PMB user if founded
     */
    Optional<PMBUser> findUserByEmail(String email);
}
