package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.TransferDTO;
import com.paymybuddy.webapp.model.constants.Response;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service used by Transfer controller
 */
public interface TransferService {

    /**
     * Verifies data and calls the registration to process a transfer.
     * @param transferDTO data entered by the user
     * @return response of the operation
     */
    Response processTransfer(TransferDTO transferDTO);

    /**
     * Registers the new transfer, updates user and beneficiary PMB account.
     * (transactional)
     * @param transferDTO data entered by the user
     * @param connexion The connexion to be used for the transfer
     * @return response of the operation
     */
    Response registerTransfer(TransferDTO transferDTO, Connexion connexion) throws Exception;
}
