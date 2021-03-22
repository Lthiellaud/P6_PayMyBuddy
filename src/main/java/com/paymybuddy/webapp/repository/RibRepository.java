package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Rib;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RibRepository extends JpaRepository<Rib, Long> {

}
