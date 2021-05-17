package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.DTO.RibDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.Rib;
import com.paymybuddy.webapp.model.constants.Response;

import java.util.List;
import java.util.Optional;

public interface RibService {

    List<Rib> getRibsByUser(PMBUser user);
    Optional<Rib> getById(Long ribId);
    Rib createRib(Rib rib);
    Optional<Rib> getByRibNameAndUser(String ribName, PMBUser user);
    Optional<Rib> getByIbanAndBicAndUser(String iban, String bic, PMBUser user);

    Response processRib(RibDTO ribDTO);
}
