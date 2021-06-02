package com.paymybuddy.webapp.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Transaction class contains the registered transactions for each connexion.
 * monetizationPC is the percentage of the commission taken on each transaction
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Transactions")
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @NotNull
    private String description;
    @NotNull
    private Date transactionDate;
    @NotNull
    private double amount;
    private double commissionPc;
    private Long billId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "connexion_id", nullable = false)
    private Connexion connexion;

}
