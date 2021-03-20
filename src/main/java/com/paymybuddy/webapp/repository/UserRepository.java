package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.PMBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<PMBUser, Long> {
    PMBUser findUserByUsername(String username);
}
