package com.paymybuddy.webapp.model.DTO;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * Data needed to add a connexion (= a beneficiary)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConnexionDTO {

    @NotBlank(message = "Please, enter a name for the connexion")
    private String connexionName;
    @NotBlank(message = "Please, enter the beneficiary email")
    private String connexionMail;
}
