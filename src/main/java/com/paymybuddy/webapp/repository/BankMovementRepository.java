package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.BankMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankMovementRepository extends JpaRepository<BankMovement, Long> {
}
