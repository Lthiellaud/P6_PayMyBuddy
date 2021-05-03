package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.Rib;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RibRepository extends JpaRepository<Rib, Long> {
    List<Rib> findAllByUser(PMBUser user);
}
