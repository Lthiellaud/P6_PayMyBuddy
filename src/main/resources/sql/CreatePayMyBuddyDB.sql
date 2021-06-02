/* Création de la databtransactionsase paymybuddy */
-- ----------------------------------- --
CREATE DATABASE if not exists `paymybuddy` ;

use paymybuddy;

/* Réinitiatlisation de la database paymybuddy */
-- ------------------------------------------- --
DROP TABLE if exists `bank_movements`;
DROP TABLE if exists `transactions`;
DROP TABLE if exists `connexions`;
DROP TABLE if exists `bank_accounts`;
DROP TABLE if exists `users`;

/* Création de la table users */
-- -------------------------- --
-- Liste des utilisateurs de la base et position de leur compte
-- (balance = montant dont il dispose dans Pay My Buddy)
-- L'identifiant utilisé est leur email
CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(55) DEFAULT NULL,
  `last_name` varchar(55) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `balance` double NOT NULL DEFAULT 0.0,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_users_email` (`email`)
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8;

/* Création de la table connexions */
-- ------------------------------- --
-- contient la liste des "connexions" créées par l'utilisateurs 
-- = liste des utilisateurs à qui il peut envoyer de l'argent
CREATE TABLE `connexions` (
  `connexion_id` bigint NOT NULL AUTO_INCREMENT,
  `connexion_name` varchar(30) DEFAULT NULL,
  `user_user_id` bigint NOT NULL,
  `beneficiary_user_id` bigint NOT NULL,
  PRIMARY KEY (`connexion_id`),
  UNIQUE KEY `UK_connexions_user_beneficiary` (`user_user_id`,`beneficiary_user_id`),
  UNIQUE KEY `UK_connexions_connexion_name` (`connexion_name`, `user_user_id`),
  CONSTRAINT `FK_connexions_users1` FOREIGN KEY (`user_user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_connexions_users2` FOREIGN KEY (`beneficiary_user_id`) REFERENCES `users` (`user_id`)
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8;

/* Création de la table bank_accounts */
-- ------------------------ --
-- Liste des comptes bancaires des utilisateurs
CREATE TABLE `bank_accounts` (
  `bank_account_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `bank_account_name` varchar(30) DEFAULT NULL,
  `account_holder` varchar(30) DEFAULT NULL,
  `bic` varchar(11) DEFAULT NULL,
  `iban` varchar(34) DEFAULT NULL,
  PRIMARY KEY (`bank_account_id`),
  CONSTRAINT `FK_rib_users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8;

/* Création de la table transactions */
-- --------------------------------- --
-- liste des transferts d'argent d'un utilisateur à un autre
-- les utilisateurs concernés par la transaction sont identifiés par leur "connexion"
-- monetizationPC est le pourcentage de commission appliqué à la transaction
CREATE TABLE `transactions` (
  `transaction_id` bigint NOT NULL AUTO_INCREMENT,
  `connexion_id` bigint NOT NULL,
  `description` varchar(40) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `commission_pc` double DEFAULT NULL,
  `bill_id` bigint DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  CONSTRAINT `FK_transactions_connexions1` FOREIGN KEY (`connexion_id`) REFERENCES `connexions` (`connexion_id`)
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8;

/* Création de la table bank_movements */
-- ----------------------------------- --
-- Liste des opérations faites par les utilisateurs pour alimenter ou vider leur compte Pay May Buddy
-- Si amount est négatif, l'utilisateur vide son compte Pay My Buddy
CREATE TABLE `bank_movements` (
  `movement_id` bigint NOT NULL AUTO_INCREMENT,
  `bank_account_id` bigint NOT NULL,
  `caption` varchar(40) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `movement_date` datetime DEFAULT NULL,
  PRIMARY KEY (`movement_id`),
  CONSTRAINT `FK_bank_movements_rib1` FOREIGN KEY (`bank_account_id`) REFERENCES `bank_accounts` (`bank_account_id`)
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8;

CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON paymybuddy.* TO 'admin'@'localhost' WITH GRANT OPTION;
