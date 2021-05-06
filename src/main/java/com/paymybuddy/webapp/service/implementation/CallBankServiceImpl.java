package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.DTO.BankExchangeDTO;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.CallBankService;
import org.springframework.stereotype.Service;

@Service
public class CallBankServiceImpl implements CallBankService {
    @Override
    public Response sendBankMovement(BankExchangeDTO bankExchangeDTO) {
        return Response.OK;
    }
}
