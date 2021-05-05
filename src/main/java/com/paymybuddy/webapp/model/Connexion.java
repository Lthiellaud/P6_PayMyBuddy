package com.paymybuddy.webapp.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Connexion contains the "friend" list for each account.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Connexions", indexes = {
        @Index(name = "uk_Connection_User_Beneficiary",
                columnList = "user_user_id, beneficiary_user_id",
                unique = true) }
)
public class Connexion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long connexionId;

    private String connexionName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_user_id", nullable = false)
    private PMBUser pmbUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "beneficiary_user_id", nullable = false)
    private PMBUser beneficiaryUser;

    @OneToMany(mappedBy = "connexion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Transaction> transactions;
}
