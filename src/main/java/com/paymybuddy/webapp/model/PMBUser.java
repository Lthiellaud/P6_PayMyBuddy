package com.paymybuddy.webapp.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * PMBUser class contains the Pay My Buddy user information.
 *  UserId, email (unique), firstname, lastname and balance (available amount)
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Users")
public class PMBUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true)
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    private double balance;

    @OneToMany(mappedBy = "pmbUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Connexion> connexions;

    @OneToMany(mappedBy = "beneficiaryUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Connexion> beneficiaryConnexions;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Rib> ribs;



}
