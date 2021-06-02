package com.paymybuddy.webapp.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Data needed to add a bank account.
 */
@Getter
@Setter
@NoArgsConstructor
public class BankAccountDTO implements Serializable {

    @NotBlank(message = "Please, give a name to your bank account")
    private String ribName;
    @Pattern(regexp = "[A-Za-z]{2}[0-9]{2}", message = "format: AA00")
    private String countryCode;
    @Pattern(regexp = "[0-9]{5}", message = "format: 12345")
    private String bankCode;
    @Pattern(regexp = "[0-9]{5}", message = "format: 12345")
    private String branchCode;
    @Pattern(regexp = "[A-Za-z0-9]{11}", message = "11 letters or numbers")
    private String accountCode;
    @Pattern(regexp = "[0-9]{2}", message = "format: 12")
    private String key;
    @Pattern(regexp = "[A-Za-z]{6}[A-Za-z0-9]{2,5}", message = "format: ABCDEF12XXX or ABCDEFYYXXX, XXX optional ")
    private String bic;
    @NotBlank(message = "Please, enter the bank account owner")
    private String accountOwner;

}
