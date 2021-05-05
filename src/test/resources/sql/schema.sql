DROP TABLE if exists `bank_movements`;
DROP TABLE if exists `transactions`;
DROP TABLE if exists `connexions`;
DROP TABLE if exists `rib`;
DROP TABLE if exists `users`;

CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(55) DEFAULT NULL,
  `last_name` varchar(55) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `balance` double DEFAULT 0.0,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_users_email` (`email`)
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8;

CREATE TABLE `connexions` (
  `connexion_id` bigint NOT NULL AUTO_INCREMENT,
  `connexion_name` varchar(30) DEFAULT NULL,
  `user_user_id` bigint NOT NULL,
  `beneficiary_user_id` bigint NOT NULL,
  PRIMARY KEY (`connexion_id`),
  UNIQUE KEY `UK_Connection_User_Beneficiary` (`user_user_id`,`beneficiary_user_id`),
  UNIQUE KEY `UK_connexions_connexion_name` (`connexion_name`, `user_user_id`),
  CONSTRAINT `FK_connexions_users1` FOREIGN KEY (`user_user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_connexions_users2` FOREIGN KEY (`beneficiary_user_id`) REFERENCES `users` (`user_id`)
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8;

CREATE TABLE `rib` (
  `rib_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `rib_name` varchar(30) DEFAULT NULL,
  `account_owner` varchar(30) DEFAULT NULL,
  `bic` varchar(8) DEFAULT NULL,
  `iban` varchar(34) DEFAULT NULL,
  PRIMARY KEY (`rib_id`),
  CONSTRAINT `FK_rib_users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8;

CREATE TABLE `transactions` (
  `transaction_id` bigint NOT NULL AUTO_INCREMENT,
  `connexion_id` bigint NOT NULL,
  `description` varchar(40) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `monetizationpc` double DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  CONSTRAINT `FK_transactions_connexions1` FOREIGN KEY (`connexion_id`) REFERENCES `connexions` (`connexion_id`)
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8;

CREATE TABLE `bank_movements` (
  `movement_id` bigint NOT NULL AUTO_INCREMENT,
  `rib_id` bigint NOT NULL,
  `caption` varchar(40) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `movement_date` datetime DEFAULT NULL,
  PRIMARY KEY (`movement_id`),
  CONSTRAINT `FK_bank_movements_rib1` FOREIGN KEY (`rib_id`) REFERENCES `rib` (`rib_id`)
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8;







