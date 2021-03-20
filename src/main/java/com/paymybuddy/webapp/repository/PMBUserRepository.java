package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.PMBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PMBUserRepository extends JpaRepository<PMBUser, Long> {
    PMBUser findUserByEmail(String email);
}
