package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.DTO.PMBUserDTO;
import com.paymybuddy.webapp.model.constants.Response;

public interface RegistrationService {
    Response register(PMBUserDTO userDTO);
}
