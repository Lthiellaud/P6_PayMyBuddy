package com.paymybuddy.webapp.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptedPasswordUtil {

        // Encrypte Password with BCryptPasswordEncoder
        public static String encrytePassword(String password) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            return encoder.encode(password);
        }

        public static void main(String[] args) {
            String password1 = "buddy1";
            String encrytedPassword1 = encrytePassword(password1);

            String password2 = "buddy2";
            String encrytedPassword2 = encrytePassword(password2);

            String password3 = "buddy3";
            String encrytedPassword3 = encrytePassword(password3);

            String password4 = "buddy4";
            String encrytedPassword4 = encrytePassword(password4);

            String password5 = "buddy5";
            String encrytedPassword5 = encrytePassword(password5);

            System.out.println("Encryted Password: buddy1 " + encrytedPassword1);
            System.out.println("Encryted Password: buddy2 " + encrytedPassword2);
            System.out.println("Encryted Password: buddy3 " + encrytedPassword3);
            System.out.println("Encryted Password: buddy4 " + encrytedPassword4);
            System.out.println("Encryted Password: buddy5 " + encrytedPassword5);
        }


}
