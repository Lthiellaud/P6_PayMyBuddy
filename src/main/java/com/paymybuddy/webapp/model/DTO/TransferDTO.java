package com.paymybuddy.webapp.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {

    private Long connexionId;
    private String description;
    private Double amount;
}
