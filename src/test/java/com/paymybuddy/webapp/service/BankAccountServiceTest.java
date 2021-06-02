package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.DTO.BankAccountDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BankAccountServiceTest {

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @MockBean
    private PMBUserService pmbUserService;

    @MockBean
    private CheckIbanAndBicService checkIbanAndBicService;

    @Autowired
    private BankAccountService bankAccountService;

    private static PMBUser currentUser;
    private static BankAccountDTO bankAccountDTO;
    private static BankAccount bankAccount;

    @BeforeEach
    public void initTest() {
        currentUser = new PMBUser();
        currentUser.setUserId(1L);
        currentUser.setEmail("current.user@mail.com");
        currentUser.setBalance(40.0);
        Set<BankAccount> bankAccounts = new HashSet<>();

        bankAccountDTO = new BankAccountDTO();
        bankAccountDTO.setBankAccountName("The bankAccount");
        bankAccountDTO.setAccountCode("1234567890a");
        bankAccountDTO.setAccountHolder("USER NAME");
        bankAccountDTO.setCountryCode("fr76");
        bankAccountDTO.setBic("AAAAAAAA");
        bankAccountDTO.setBankCode("11111");
        bankAccountDTO.setBranchCode("22222");
        bankAccountDTO.setKey("00");

        bankAccount = new BankAccount();
        bankAccount.setBankAccountId(1L);
        bankAccount.setBic("AAAAAAAA");
        bankAccount.setIban("FR7611111222221234567890A00");
        bankAccount.setUser(currentUser);
        bankAccount.setBankAccountName("The bankAccount");
        bankAccount.setAccountHolder("");

    }

    @Test
    public void processBankAccountTest() {
        when(pmbUserService.getCurrentUser()).thenReturn(currentUser);
        when(bankAccountRepository.findByBankAccountNameAndUser(bankAccountDTO.getBankAccountName(), currentUser))
                .thenReturn(Optional.empty());
        when(bankAccountRepository.findByIbanAndBicAndUser("FR7611111222221234567890A00" ,"AAAAAAAA", currentUser))
                .thenReturn(Optional.empty());
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(bankAccount);
        when(checkIbanAndBicService.checkIbanAndBic("FR7611111222221234567890A00", bankAccountDTO.getBic()))
                .thenReturn(Response.OK);

        Response response = bankAccountService.processBankAccount(bankAccountDTO);

        assertThat(response).isEqualTo(Response.OK);
    }

    @Test
    public void processBankAccountSAVE_KOTest() {

        when(pmbUserService.getCurrentUser()).thenReturn(currentUser);
        when(bankAccountRepository.findByBankAccountNameAndUser(bankAccountDTO.getBankAccountName(), currentUser))
                .thenReturn(Optional.empty());
        when(bankAccountRepository.findByIbanAndBicAndUser("FR7611111222221234567890A00" ,"AAAAAAAA", currentUser))
                .thenReturn(Optional.empty());
        when(checkIbanAndBicService.checkIbanAndBic("FR7611111222221234567890A00", bankAccountDTO.getBic()))
                .thenReturn(Response.OK);
        when(bankAccountRepository.save(any(BankAccount.class))).thenThrow(RuntimeException.class);

        Response response = bankAccountService.processBankAccount(bankAccountDTO);

        verify(pmbUserService, times(1)).getCurrentUser();
        verify(bankAccountRepository, times(1)).findByBankAccountNameAndUser(bankAccountDTO.getBankAccountName(), currentUser);
        verify(bankAccountRepository, times(1)).findByIbanAndBicAndUser("FR7611111222221234567890A00" ,"AAAAAAAA", currentUser);
        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
        assertThat(response).isEqualTo(Response.SAVE_KO);

    }

    @Test
    public void processTransferWhenBankAccountIsFoundShouldReturnEXISTING_IBAN() {
        when(pmbUserService.getCurrentUser()).thenReturn(currentUser);
        when(bankAccountRepository.findByBankAccountNameAndUser(bankAccountDTO.getBankAccountName(), currentUser))
                .thenReturn(Optional.empty());
        when(bankAccountRepository.findByIbanAndBicAndUser("FR7611111222221234567890A00" ,"AAAAAAAA", currentUser))
                .thenReturn(Optional.of(bankAccount));
        Response response = bankAccountService.processBankAccount(bankAccountDTO);
        assertThat(response).isEqualTo(Response.EXISTING_IBAN);

    }
    @Test
    public void processTransferWithBeneficiaryNotFoundShouldReturnIBAN_KO() {
        when(pmbUserService.getCurrentUser()).thenReturn(currentUser);
        when(bankAccountRepository.findByBankAccountNameAndUser(bankAccountDTO.getBankAccountName(), currentUser))
                .thenReturn(Optional.empty());
        when(bankAccountRepository.findByIbanAndBicAndUser("FR7611111222221234567890A00" ,"AAAAAAAA", currentUser))
                .thenReturn(Optional.empty());
        when(checkIbanAndBicService.checkIbanAndBic("FR7611111222221234567890A00", bankAccountDTO.getBic()))
                .thenReturn(Response.IBAN_BIC_KO);

        Response response = bankAccountService.processBankAccount(bankAccountDTO);

        assertThat(response).isEqualTo(Response.IBAN_BIC_KO);

    }

    @Test
    public void processTransferWithExistingNameShouldReturnEXISTING_RIB_NAME() {
        //Response TransferDTO transferDTO, BankAccount bankAccount
        when(pmbUserService.getCurrentUser()).thenReturn(currentUser);
        when(bankAccountRepository.findByBankAccountNameAndUser(bankAccountDTO.getBankAccountName(), currentUser))
                .thenReturn(Optional.of(bankAccount));

        Response response = bankAccountService.processBankAccount(bankAccountDTO);

        assertThat(response).isEqualTo(Response.EXISTING_BANK_ACCOUNT_NAME);

    }


}
