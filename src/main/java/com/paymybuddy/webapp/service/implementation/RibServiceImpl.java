package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.DTO.BankAccountDTO;
import com.paymybuddy.webapp.model.PMBUser;
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
    public List<BankAccount> getRibsByUser(PMBUser user) {
        return ribRepository.findAllByUser(user);
    }

    @Override
    public Optional<BankAccount> getById(Long ribId) {
        return ribRepository.findById(ribId);
    }

    @Override
    public BankAccount createRib(BankAccount bankAccount) {
        LOGGER.debug("BankAccount to be created name " + bankAccount.getRibName() + "IBAN : " + bankAccount.getIban()
                + ", BIC : " + bankAccount.getBic() + ", account owner : " + bankAccount.getAccountOwner());
        return ribRepository.save(bankAccount);
    }

    @Override
    public Optional<BankAccount> getByRibNameAndUser(String ribName, PMBUser user) {
        return ribRepository.findByRibNameAndUser(ribName, user);
    }

    @Override
    public Optional<BankAccount> getByIbanAndBicAndUser(String iban, String bic, PMBUser user) {
        return ribRepository.findByIbanAndBicAndUser(iban, bic, user);
    }

    private void putUpperCase(BankAccountDTO bankAccountDTO) {
        bankAccountDTO.setAccountCode(bankAccountDTO.getAccountCode().toUpperCase());
        bankAccountDTO.setAccountOwner(bankAccountDTO.getAccountOwner().toUpperCase());
        bankAccountDTO.setCountryCode(bankAccountDTO.getCountryCode().toUpperCase());
        bankAccountDTO.setBic(bankAccountDTO.getBic().toUpperCase());

    }

    @Override
    public Response processRib(BankAccountDTO bankAccountDTO) {
        putUpperCase(bankAccountDTO);
        PMBUser currentUser = pmbUserService.getCurrentUser();
        if (getByRibNameAndUser(bankAccountDTO.getRibName(), currentUser).isPresent()) {
            LOGGER.debug(Response.EXISTING_RIB_NAME + "for current user : " + currentUser.getEmail()
                    + ", rib name : " + bankAccountDTO.getRibName());
            return Response.EXISTING_RIB_NAME;
        }
        String iban = bankAccountDTO.getCountryCode() + bankAccountDTO.getBankCode()
                + bankAccountDTO.getBranchCode() + bankAccountDTO.getAccountCode()
                + bankAccountDTO.getKey();
        if (getByIbanAndBicAndUser(iban, bankAccountDTO.getBic(), currentUser).isPresent()) {
            LOGGER.debug(Response.EXISTING_IBAN + ": current user : " + currentUser.getEmail()
                    + ", IBAN : " + iban + ", BIC : " + bankAccountDTO.getBic());
            return Response.EXISTING_IBAN;
        }
        if (checkIbanAndBicService.checkIbanAndBic(iban, bankAccountDTO.getBic()) != Response.OK) {
            return Response.IBAN_BIC_KO;
        }
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.setUser(currentUser);
        newBankAccount.setRibName(bankAccountDTO.getRibName());
        newBankAccount.setAccountOwner(bankAccountDTO.getAccountOwner());
        newBankAccount.setIban(iban);
        newBankAccount.setBic(bankAccountDTO.getBic());
        try {
            createRib(newBankAccount);
        } catch (Exception e) {
            return Response.SAVE_KO;
        }
        LOGGER.debug("BankAccount created id " + newBankAccount.getRibId());

        return Response.OK;
    }
}
