package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.DTO.BankExchangeDTO;
import com.paymybuddy.webapp.model.constants.Response;

public interface CallBankService {

    Response sendBankMovement(BankExchangeDTO bankExchangeDTO);
}
