package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.PMBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PMBUserRepository extends JpaRepository<PMBUser, Long> {

    Optional<PMBUser> findUserByEmail(String email);
}
