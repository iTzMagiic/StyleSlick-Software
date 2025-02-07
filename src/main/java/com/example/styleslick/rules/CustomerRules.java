package com.example.styleslick.rules;

import com.example.styleslick.service.AlertService;

import java.util.Map;


public class CustomerRules {


    public boolean isNotAllowedToAddCustomer(Map<String, String> filledFields) {

        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Bitte geben Sie die Pflichtfelder ein.");
            return true;
        }

        if (!filledFields.containsKey("username")) {
            AlertService.showErrorAlert("Bitte geben Sie einen Benutzernamen an.");
            return true;
        }

        if (!filledFields.containsKey("purchased_from")) {
            AlertService.showErrorAlert("Bitte geben Sie eine Platform über der Gekauft wurden ist an.");
            return true;
        }

        if (filledFields.containsKey("postal_code") && isNotValidPostalCode(filledFields.get("postal_code"))) {
            return true;
        }

        if (!filledFields.containsKey("country")) {
            AlertService.showErrorAlert("Bitte geben Sie ein Land an.");
            return true;
        }

        if (isNotValidCountry(filledFields.get("country"))) {
            return true;
        }

        return false;
    }


    public boolean isNotAllowedToSearchCustomer(Map<String, String> filledFields) {

        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Bitte mindestens Ein Feld ausfüllen.");
            return true;
        }

        if (filledFields.containsKey("country") && isNotValidCountry(filledFields.get("country"))) {
            return true;
        }

        return false;
    }


    public boolean isNotAllowedToUpdateCustomer(Map<String, String> filledFields) {

        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Bitte geben Sie etwas an um den Kunden zu bearbeiten.");
            return true;
        }

        if (filledFields.containsKey("postal_code") && isNotValidPostalCode(filledFields.get("postal_code"))) {
            return true;
        }

        if (filledFields.containsKey("country") && isNotValidCountry(filledFields.get("country"))) {
            return true;
        }

        return false;
    }


    private boolean isNotValidPostalCode(String postalCode) {
        if (!postalCode.matches("\\d{5}")) {
            AlertService.showErrorAlert("Die Postleitzahl darf nur aus Zahlen bestehen und muss 5-stellig sein.");
            return true;
        }
        return false;
    }


    private boolean isNotValidCustomerNumber(String customerNumber) {
        return !customerNumber.matches("^C\\d{8}$");
    }

    private boolean isNotValidCountry(String country) {
        if (!country.matches("^[a-zA-Z]+$")) {
            AlertService.showErrorAlert("Bitte geben Sie ein gültiges Land ein.");
            return true;
        }
        return false;
    }

}
