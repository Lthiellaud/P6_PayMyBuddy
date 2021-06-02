package com.paymybuddy.webapp.model.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * PMBUserDTO class contains the data entered on registrationPage.
 *  UserId, email (unique), firstname, lastname, password, confirmPassword
 */
@Getter
@Setter
public class PMBUserDTO {

    @NotBlank(message = "email is mandatory, it will be used as your username to log in PayMyBuddy")
    private String email;
    @NotBlank(message = "First name is mandatory")
    private String firstName;
    @NotBlank(message = "Last name is mandatory")
    private String lastName;
    @Size(min = 8, max = 16, message = "Your password should have between 8 and 16 characters")
    private String password;
    private String repeatPassword;


}
