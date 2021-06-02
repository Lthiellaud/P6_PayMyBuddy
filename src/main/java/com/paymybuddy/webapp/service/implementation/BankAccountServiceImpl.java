package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.DTO.BankAccountDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.repository.BankAccountRepository;
import com.paymybuddy.webapp.service.CheckIbanAndBicService;
import com.paymybuddy.webapp.service.PMBUserService;
import com.paymybuddy.webapp.service.BankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private PMBUserService pmbUserService;

    @Autowired
    private CheckIbanAndBicService checkIbanAndBicService;

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    @Override
    public List<BankAccount> getBankAccountsByUser(PMBUser user) {
        return bankAccountRepository.findAllByUser(user);
    }

    @Override
    public Optional<BankAccount> getById(Long bankAccountId) {
        return bankAccountRepository.findById(bankAccountId);
    }

    @Override
    public BankAccount createBankAccount(BankAccount bankAccount) {
        LOGGER.debug("BankAccount to be created name " + bankAccount.getBankAccountName() + "IBAN : " + bankAccount.getIban()
                + ", BIC : " + bankAccount.getBic() + ", account owner : " + bankAccount.getAccountHolder());
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public Optional<BankAccount> getByBankAccountNameAndUser(String bankAccountName, PMBUser user) {
        return bankAccountRepository.findByBankAccountNameAndUser(bankAccountName, user);
    }

    @Override
    public Optional<BankAccount> getByIbanAndBicAndUser(String iban, String bic, PMBUser user) {
        return bankAccountRepository.findByIbanAndBicAndUser(iban, bic, user);
    }

    private void putUpperCase(BankAccountDTO bankAccountDTO) {
        bankAccountDTO.setAccountCode(bankAccountDTO.getAccountCode().toUpperCase());
        bankAccountDTO.setAccountHolder(bankAccountDTO.getAccountHolder().toUpperCase());
        bankAccountDTO.setCountryCode(bankAccountDTO.getCountryCode().toUpperCase());
        bankAccountDTO.setBic(bankAccountDTO.getBic().toUpperCase());

    }

    @Override
    public Response processBankAccount(BankAccountDTO bankAccountDTO) {
        putUpperCase(bankAccountDTO);
        PMBUser currentUser = pmbUserService.getCurrentUser();
        if (getByBankAccountNameAndUser(bankAccountDTO.getBankAccountName(), currentUser).isPresent()) {
            LOGGER.debug(Response.EXISTING_BANK_ACCOUNT_NAME + "for current user : " + currentUser.getEmail()
                    + ", rib name : " + bankAccountDTO.getBankAccountName());
            return Response.EXISTING_BANK_ACCOUNT_NAME;
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
        newBankAccount.setBankAccountName(bankAccountDTO.getBankAccountName());
        newBankAccount.setAccountHolder(bankAccountDTO.getAccountHolder());
        newBankAccount.setIban(iban);
        newBankAccount.setBic(bankAccountDTO.getBic());
        try {
            createBankAccount(newBankAccount);
        } catch (Exception e) {
            return Response.SAVE_KO;
        }
        LOGGER.debug("BankAccount created id " + newBankAccount.getBankAccountId());

        return Response.OK;
    }
}
