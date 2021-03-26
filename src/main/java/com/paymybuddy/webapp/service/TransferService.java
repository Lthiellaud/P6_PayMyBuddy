package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.TransferDTO;
import com.paymybuddy.webapp.model.constants.Response;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransferService {
    List<Connexion> getUserConnexion();

    Response processTransfer(TransferDTO transferDTO);

    @Transactional
    Response registerTransfer(TransferDTO transferDTO, Connexion connexion);
}
