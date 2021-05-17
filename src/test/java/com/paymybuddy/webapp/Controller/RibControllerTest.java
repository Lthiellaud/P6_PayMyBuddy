package com.paymybuddy.webapp.Controller;

import com.paymybuddy.webapp.controller.RibController;
import com.paymybuddy.webapp.model.Rib;
import com.paymybuddy.webapp.service.PMBSharedService;
import com.paymybuddy.webapp.service.PMBUserService;
import com.paymybuddy.webapp.service.RibService;
import com.paymybuddy.webapp.service.implementation.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RibController.class)
public class RibControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PMBUserService pmbUserService;

    @MockBean
    private RibService ribService;

    @MockBean
    private PMBSharedService pmbSharedService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void getRibPageWithoutAuthenticationTest() throws Exception {
        mockMvc.perform(get("/home/rib"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("http://localhost/login"));

    }

    @WithMockUser
    @Test
    public void getRibPageTest() throws Exception {
        List<Rib> ribList = new ArrayList<>();
        mockMvc.perform(get("/home/rib"))
                .andExpect(status().isOk())
                .andExpect(view().name("ribPage"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("ribs", ribList));

    }

    @WithMockUser
    @Test
    public void addRibWithBlankNameTest() throws Exception {
        mockMvc.perform(post("/home/rib")
                .param("ribName", "")
                .param("accountOwner", "USER")
                .param("countryCode", "fr76")
                .param("bankCode", "11111")
                .param("branchCode", "11111")
                .param("AccountCode", "1234567890f")
                .param("key", "55")
                .param("bic", "ABCDEFGH"))
                .andExpect(status().isOk())
                .andExpect(view().name("ribPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("ribDTO", "ribName", "NotBlank"));

    }

    @WithMockUser
    @Test
    public void addRibWithBlankOwnerTest() throws Exception {
        mockMvc.perform(post("/home/rib")
                .param("ribName", "My RIB")
                .param("accountOwner", "")
                .param("countryCode", "fr76")
                .param("bankCode", "11111")
                .param("branchCode", "11111")
                .param("AccountCode", "1234567890f")
                .param("key", "55")
                .param("bic", "ABCDEFGH"))
                .andExpect(status().isOk())
                .andExpect(view().name("ribPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("ribDTO", "accountOwner", "NotBlank"));

    }

    @WithMockUser
    @Test
    public void addRibWithFormatIssuesTest() throws Exception {
        mockMvc.perform(post("/home/rib")
                .param("ribName", "My RIB")
                .param("accountOwner", "USER")
                .param("countryCode", "")
                .param("bankCode", "111112")
                .param("branchCode", "111k")
                .param("accountCode", "1234567890fm")
                .param("key", "ab")
                .param("bic", "ABCDEFGH1225"))
                .andExpect(status().isOk())
                .andExpect(view().name("ribPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("ribDTO", "countryCode", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("ribDTO", "bankCode", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("ribDTO", "branchCode", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("ribDTO", "accountCode", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("ribDTO", "key", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("ribDTO", "bic", "Pattern"));

    }


}
