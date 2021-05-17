package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.DTO.RibDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.Rib;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.repository.RibRepository;
import com.paymybuddy.webapp.service.CheckIbanAndBicService;
import com.paymybuddy.webapp.service.PMBUserService;
import com.paymybuddy.webapp.service.RibService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RibServiceImpl implements RibService {

    @Autowired
    private RibRepository ribRepository;

    @Autowired
    private PMBUserService pmbUserService;

    @Autowired
    private CheckIbanAndBicService checkIbanAndBicService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RibServiceImpl.class);

    @Override
    public List<Rib> getRibsByUser(PMBUser user) {
        List<Rib> ribs = ribRepository.findAllByUser(user);
        return ribs;
    }

    @Override
    public Optional<Rib> getById(Long ribId) {
        return ribRepository.findById(ribId);
    }

    @Override
    public Rib createRib(Rib rib) {
        LOGGER.debug("Rib to be created name " + rib.getRibName() + "IBAN : " + rib.getIban()
                + ", BIC : " + rib.getBic() + ", account owner : " + rib.getAccountOwner());
        return ribRepository.save(rib);
    }

    @Override
    public Optional<Rib> getByRibNameAndUser(String ribName, PMBUser user) {
        return ribRepository.findByRibNameAndUser(ribName, user);
    }

    @Override
    public Optional<Rib> getByIbanAndBicAndUser(String iban, String bic, PMBUser user) {
        return ribRepository.findByIbanAndBicAndUser(iban, bic, user);
    }

    private void putUpperCase(RibDTO ribDTO) {
        ribDTO.setAccountCode(ribDTO.getAccountCode().toUpperCase());
        ribDTO.setAccountOwner(ribDTO.getAccountOwner().toUpperCase());
        ribDTO.setCountryCode(ribDTO.getCountryCode().toUpperCase());
        ribDTO.setBic(ribDTO.getBic().toUpperCase());

    }

    @Override
    public Response processRib(RibDTO ribDTO) {
        putUpperCase(ribDTO);
        PMBUser currentUser = pmbUserService.getCurrentUser();
        if (getByRibNameAndUser(ribDTO.getRibName(), currentUser).isPresent()) {
            LOGGER.debug(Response.EXISTING_RIB_NAME + ": current user : " + currentUser.getEmail()
                    + ", rib name : " + ribDTO.getRibName());
            return Response.EXISTING_RIB_NAME;
        }
        String iban = ribDTO.getCountryCode() + ribDTO.getBankCode()
                + ribDTO.getBranchCode() + ribDTO.getAccountCode()
                + ribDTO.getKey();
        if (getByIbanAndBicAndUser(iban, ribDTO.getBic(), currentUser).isPresent()) {
            LOGGER.debug(Response.EXISTING_IBAN + ": current user : " + currentUser.getEmail()
                    + ", IBAN : " + iban + ", BIC : " + ribDTO.getBic());
            return Response.EXISTING_IBAN;
        }
        if (checkIbanAndBicService.checkIbanAndBic(iban, ribDTO.getBic()) != Response.OK) {
            return Response.IBAN_BIC_KO;
        }
        Rib newRib = new Rib();
        newRib.setUser(currentUser);
        newRib.setRibName(ribDTO.getRibName());
        newRib.setAccountOwner(ribDTO.getAccountOwner());
        newRib.setIban(iban);
        newRib.setBic(ribDTO.getBic());
        try {
            createRib(newRib);
        } catch (Exception e) {
            return Response.SAVE_KO;
        }
        LOGGER.debug("Rib created id " + newRib.getRibId());

        return Response.OK;
    }
}
