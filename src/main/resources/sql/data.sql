INSERT INTO paymybuddy.users ( email, first_name, last_name, password, balance) 
VALUE ("lol.buddy@mail.com", "Lol", "Buddy1","$2a$10$tx/gdzaivf4E8x5sxF827.8aLO/iI8tjNqiatdnlhRPXgskz2HY4S", 200.0), 
	("tom.buddy@mail.com", "Tom", "Buddy2","$2a$10$p.BoCPmuC2SIMApc2yQ1b.LQUGVJYrqBhXmbuKGCj0XDxR0nV1lKW", 200.0), 
	("john.buddy@mail.com", "John", "Buddy3","$2a$10$vZMl7Dy52L08ZH8HZpWTSelmXJhoQ1znU3xaN3n/JALU7/EE.BMDK", 200.0), 
	("thierry.buddy@mail.com", "Thierry", "Buddy4","$2a$10$jzEaZPwPTHjdWtNxRLgNlueUhhUdNk.s95PqsMGfNeY2DgyrePcGy", 200.0), 
	("madeleine.buddy@mail.com", "Madeleine", "Buddy5","$2a$10$.Ud5vYOthEJm6LyEgHwoaOzaFAVrO.PcVtU1.PcLubnPlXfHSVz12", 200.0);

INSERT INTO paymybuddy.connexions ( connexion_name, user_user_id, beneficiary_user_id )
VALUE ("My connexion", 1, 2);

INSERT INTO paymybuddy.bank_accounts ( user_id, rib_name, account_owner, bic, iban)
VALUE (1, "My Rib", "LOL BUDDY", "XXXXXXYY", "FR7622222888889999977777X00");