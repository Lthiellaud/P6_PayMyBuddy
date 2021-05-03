package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.DTO.BankMovementDTO;
import com.paymybuddy.webapp.model.Rib;
import com.paymybuddy.webapp.model.constants.Response;

public interface CallBankService {

    Response sendBankMovement(BankMovementDTO bankMovementDTO);
}
