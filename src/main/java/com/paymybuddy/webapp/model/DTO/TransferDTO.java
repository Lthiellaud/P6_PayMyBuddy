package com.paymybuddy.webapp.model.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class TransferDTO {

    @Positive(message = "Please, select a connexion")
    private Long connexionId;
    @NotBlank(message = "Please, enter a description")
    private String description;
    @Positive(message = "Please, enter a positive amount")
    private double amount;

}
