package com.paymybuddy.webapp.model.constants;

public enum Response {
    OK("Successful operation"),
    NOT_ENOUGH_MONEY("You haven't enough money on your account"),
    SAVE_KO("A problem occurs, please try later"),
    MAIL_NOT_FOUND("This email is unknown in Pay my Buddy "),
    EXISTING_CONNEXION("This email is already used for a connexion"),
    EXISTING_CONNEXION_NAME("This connexion name already exists"),
    EXISTING_IBAN("This IBAN / BIC is already registered"),
    EXISTING_RIB_NAME("This rib name already exists"),
    IBAN_BIC_KO("IBAN / BIC is not correct"),
    DATA_ISSUE("A problem occurs, please try again later"),
    BANK_EXCHANGE_ISSUE("A problem occurred during the exchange with your bank, please try again later");


    private String message;

    Response(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
