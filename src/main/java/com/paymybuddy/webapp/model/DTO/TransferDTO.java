package com.paymybuddy.webapp.model.DTO;

import lombok.Data;

@Data
public class TransferDTO {

    private Long connexionId;
    private String description;
    private double amount;
}
