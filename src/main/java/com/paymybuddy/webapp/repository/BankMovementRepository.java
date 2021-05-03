package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.BankMovement;
import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.Rib;
import com.paymybuddy.webapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankMovementRepository extends JpaRepository<BankMovement, Long> {
    List<BankMovement> findAllByRibInOrderByMovementDateDesc(List<Rib> Ribs);

}
