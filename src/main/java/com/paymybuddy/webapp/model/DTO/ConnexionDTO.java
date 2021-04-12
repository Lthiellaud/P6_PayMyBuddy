package com.paymybuddy.webapp.model.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ConnexionDTO {

    @NotBlank(message = "Please, enter a name for the connexion")
    private String connexionName;
    @NotBlank(message = "Please, enter the beneficiary email")
    private String connexionMail;
}
