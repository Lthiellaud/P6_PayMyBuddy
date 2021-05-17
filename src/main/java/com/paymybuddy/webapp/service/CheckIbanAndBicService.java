package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.constants.Response;

public interface CheckIbanAndBicService {

    Response checkIbanAndBic(String iban, String bic) ;
}
