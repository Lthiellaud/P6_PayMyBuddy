package com.paymybuddy.webapp.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OperationDTO {
    @Positive(message = "Please, select a bank account")
    private Long ribId;
    @Positive(message = "Please, select an operation")
    private Long debitCredit;
    @Positive(message = "Please, enter a positive amount")
    private double amount;
}