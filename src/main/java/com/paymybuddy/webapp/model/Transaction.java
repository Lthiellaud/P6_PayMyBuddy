package com.paymybuddy.webapp.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Transaction class contains the registered transactions for each connexion.
 * monetizationPC is the percentage of the commission taken on each transaction
 */
@Entity
@Data
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
    private Double amount;
    private Float monetizationPC;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "connexion_id", nullable = false)
    private Connexion connexion;

}
