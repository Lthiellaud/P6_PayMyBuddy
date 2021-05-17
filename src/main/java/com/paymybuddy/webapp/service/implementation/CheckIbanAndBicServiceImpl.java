package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.CheckIbanAndBicService;
import org.springframework.stereotype.Service;

@Service
public class CheckIbanAndBicServiceImpl implements CheckIbanAndBicService {

    @Override
    public Response checkIbanAndBic(String iban, String bic) {
        return Response.OK;
    }
}
