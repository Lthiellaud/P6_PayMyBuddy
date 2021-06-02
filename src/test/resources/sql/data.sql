INSERT INTO testmybuddy.users ( email, first_name, last_name, password, balance)
VALUE ("lol.buddy@mail.com", "Lol", "Buddy1","$2a$10$tx/gdzaivf4E8x5sxF827.8aLO/iI8tjNqiatdnlhRPXgskz2HY4S", 200.0), 
	("tom.buddy@mail.com", "Tom", "Buddy2","$2a$10$p.BoCPmuC2SIMApc2yQ1b.LQUGVJYrqBhXmbuKGCj0XDxR0nV1lKW", 200.0), 
	("john.buddy@mail.com", "John", "Buddy3","$2a$10$vZMl7Dy52L08ZH8HZpWTSelmXJhoQ1znU3xaN3n/JALU7/EE.BMDK", 200.0), 
	("thierry.buddy@mail.com", "Thierry", "Buddy4","$2a$10$jzEaZPwPTHjdWtNxRLgNlueUhhUdNk.s95PqsMGfNeY2DgyrePcGy", 200.0), 
	("madeleine.buddy@mail.com", "Madeleine", "Buddy5","$2a$10$.Ud5vYOthEJm6LyEgHwoaOzaFAVrO.PcVtU1.PcLubnPlXfHSVz12", 200.0);

INSERT INTO testmybuddy.connexions ( connexion_name, user_user_id, beneficiary_user_id )
VALUE ("My connexion to Tom", 1, 2),
      ("My connexion to John", 1, 3);

INSERT INTO testmybuddy.bank_accounts ( user_id, bank_account_name, account_holder, bic, iban )
VALUE (1, "My Rib", "LOL BUDDY", "XXXXXXXX", "FR7622222888889999977777X00");

INSERT INTO testmybuddy.transactions ( connexion_id, description, amount, transaction_date, commission_pc )
VALUE ( 1, "First transaction", 10.0, "2021-05-01", 0.5),
      ( 2, "Second transaction", 20.0, "2021-05-02", 0.5);