package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.DTO.OperationDTO;
import com.paymybuddy.webapp.model.Rib;
import com.paymybuddy.webapp.model.constants.Response;

public interface AccountService {
    Response operateMovement(OperationDTO operationDTO);
    Response registerMovement(OperationDTO operationDTO, Rib rib);
}
