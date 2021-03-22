package com.paymybuddy.webapp.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Connexion contains the "friend" list for each account.
 */
@Entity
@Data
@Table(name = "Connexions", indexes = {
        @Index(name = "uk_Connection_User_Beneficiary",
                columnList = "user_id, beneficiary_user_id",
                unique = true) }
)
public class Connexion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long connexionId;
    @Column(unique = true)
    private String connexionName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private PMBUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "beneficiary_user_id", nullable = false)
    private PMBUser beneficiaryUser;

    @OneToMany(mappedBy = "connexion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Transaction> transactions;
}
