package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.DTO.RibDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.Rib;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.repository.RibRepository;
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
public class RibServiceTest {

    @MockBean
    private RibRepository ribRepository;

    @MockBean
    private PMBUserService pmbUserService;

    @MockBean
    private CheckIbanAndBicService checkIbanAndBicService;

    @Autowired
    private RibService ribService;

    private static PMBUser currentUser;
    private static RibDTO ribDTO;
    private static Rib rib;

    @BeforeEach
    public void initTest() {
        currentUser = new PMBUser();
        currentUser.setUserId(1L);
        currentUser.setEmail("current.user@mail.com");
        currentUser.setBalance(40.0);
        Set<Rib> ribs = new HashSet<>();

        ribDTO = new RibDTO();
        ribDTO.setRibName("The rib");
        ribDTO.setAccountCode("1234567890a");
        ribDTO.setAccountOwner("USER NAME");
        ribDTO.setCountryCode("fr76");
        ribDTO.setBic("AAAAAAAA");
        ribDTO.setBankCode("11111");
        ribDTO.setBranchCode("22222");
        ribDTO.setKey("00");

        rib = new Rib();
        rib.setRibId(1L);
        rib.setBic("AAAAAAAA");
        rib.setIban("FR7611111222221234567890A00");
        rib.setUser(currentUser);
        rib.setRibName("The rib");
        rib.setAccountOwner("");

    }

    @Test
    public void processRibTest() {
        when(pmbUserService.getCurrentUser()).thenReturn(currentUser);
        when(ribRepository.findByRibNameAndUser(ribDTO.getRibName(), currentUser))
                .thenReturn(Optional.empty());
        when(ribRepository.findByIbanAndBicAndUser("FR7611111222221234567890A00" ,"AAAAAAAA", currentUser))
                .thenReturn(Optional.empty());
        when(ribRepository.save(any(Rib.class))).thenReturn(rib);
        when(checkIbanAndBicService.checkIbanAndBic("FR7611111222221234567890A00", ribDTO.getBic()))
                .thenReturn(Response.OK);

        Response response = ribService.processRib(ribDTO);

        assertThat(response).isEqualTo(Response.OK);
    }

    @Test
    public void processRibSAVE_KOTest() throws Exception {

        when(pmbUserService.getCurrentUser()).thenReturn(currentUser);
        when(ribRepository.findByRibNameAndUser(ribDTO.getRibName(), currentUser))
                .thenReturn(Optional.empty());
        when(ribRepository.findByIbanAndBicAndUser("FR7611111222221234567890A00" ,"AAAAAAAA", currentUser))
                .thenReturn(Optional.empty());
        when(checkIbanAndBicService.checkIbanAndBic("FR7611111222221234567890A00", ribDTO.getBic()))
                .thenReturn(Response.OK);
        when(ribRepository.save(any(Rib.class))).thenThrow(RuntimeException.class);

        Response response = ribService.processRib(ribDTO);

        verify(pmbUserService, times(1)).getCurrentUser();
        verify(ribRepository, times(1)).findByRibNameAndUser(ribDTO.getRibName(), currentUser);
        verify(ribRepository, times(1)).findByIbanAndBicAndUser("FR7611111222221234567890A00" ,"AAAAAAAA", currentUser);
        verify(ribRepository, times(1)).save(any(Rib.class));
        assertThat(response).isEqualTo(Response.SAVE_KO);

    }

    @Test
    public void processTransferWhenRibIsFoundShouldReturnEXISTING_IBAN() throws Exception {
        when(pmbUserService.getCurrentUser()).thenReturn(currentUser);
        when(ribRepository.findByRibNameAndUser(ribDTO.getRibName(), currentUser))
                .thenReturn(Optional.empty());
        when(ribRepository.findByIbanAndBicAndUser("FR7611111222221234567890A00" ,"AAAAAAAA", currentUser))
                .thenReturn(Optional.of(rib));
        Response response = ribService.processRib(ribDTO);
        assertThat(response).isEqualTo(Response.EXISTING_IBAN);

    }
    @Test
    public void processTransferWithBeneficiaryNotFoundShouldReturnIBAN_KO() throws Exception {
        when(pmbUserService.getCurrentUser()).thenReturn(currentUser);
        when(ribRepository.findByRibNameAndUser(ribDTO.getRibName(), currentUser))
                .thenReturn(Optional.empty());
        when(ribRepository.findByIbanAndBicAndUser("FR7611111222221234567890A00" ,"AAAAAAAA", currentUser))
                .thenReturn(Optional.empty());
        when(checkIbanAndBicService.checkIbanAndBic("FR7611111222221234567890A00", ribDTO.getBic()))
                .thenReturn(Response.IBAN_BIC_KO);

        Response response = ribService.processRib(ribDTO);

        assertThat(response).isEqualTo(Response.IBAN_BIC_KO);

    }

    @Test
    public void processTransferWithExistingNameShouldReturnEXISTING_RIB_NAME() throws Exception {
        //Response TransferDTO transferDTO, Rib rib
        when(pmbUserService.getCurrentUser()).thenReturn(currentUser);
        when(ribRepository.findByRibNameAndUser(ribDTO.getRibName(), currentUser))
                .thenReturn(Optional.of(rib));

        Response response = ribService.processRib(ribDTO);

        assertThat(response).isEqualTo(Response.EXISTING_RIB_NAME);

    }


}
