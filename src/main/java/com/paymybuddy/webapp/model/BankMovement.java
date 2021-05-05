package com.paymybuddy.webapp.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * ExternalMovement registers the movement with a bank account on the PayMyBuddy account.
 * isCredit is true if money is sent from an bank account to the PayMyBuddy account.
 * It is false if money is retrieved from the PayMyBuddy account.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Bank_movements")
public class BankMovement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movementId;
    private String caption;
    private Date movementDate;
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rib_id", nullable = false)
    private Rib rib;
}
