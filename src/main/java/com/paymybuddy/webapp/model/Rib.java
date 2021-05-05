package com.paymybuddy.webapp.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Rib class defines the RIBs available for a user.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Rib")
public class Rib implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ribId;
    private String ribName;
    private String iban;
    private String bic;
    private String accountOwner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private PMBUser user;

    @OneToMany(mappedBy = "rib", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BankMovement> bankMovements;
}
