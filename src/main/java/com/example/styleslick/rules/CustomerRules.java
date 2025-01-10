package com.example.styleslick.rules;

import com.example.styleslick.service.RulesService;

import java.util.Map;

public class CustomerRules {


    public boolean isAllowedToAddCustomer(Map<String, String> filledFields) {

        if (filledFields.isEmpty()) {
            RulesService.showErrorAlert("Bitte geben Sie was ein.");
            return false;
        }

        if (!filledFields.containsKey("username")) {
            RulesService.showErrorAlert("Bitte geben Sie einen Benutzernamen an.");
            return false;
        }

        if (!filledFields.containsKey("purchased_from")) {
            RulesService.showErrorAlert("Bitte geben Sie eine Platform über der Gekauft wurden ist an.");
            return false;
        }

        if (filledFields.containsKey("postal_code") && !isValidPostalCode(filledFields.get("postal_code"))) {
            return false;
        }


        if (!filledFields.containsKey("country")) {
            RulesService.showErrorAlert("Bitte geben Sie ein Land an.");
            return false;
        }

        if (!isValidCountry(filledFields.get("country"))) {
            return false;
        }

        return true;
    }

    public boolean isAllowedToSearchCustomer(Map<String, String> filledFields) {

        if (filledFields.isEmpty()) {
            RulesService.showErrorAlert("Bitte mindestens Ein Feld ausfüllen.");
            return false;
        }

        if (filledFields.containsKey("postal_code") && !isValidPostalCode(filledFields.get("postal_code"))) {
            return false;
        }

        if (filledFields.containsKey("country") && !isValidCountry(filledFields.get("country"))) {
            return false;
        }

        return true;
    }



    private  boolean isValidPostalCode(String postalCode) {
        /*
        TODO:: PLZ in der Datenbank auf VARCHAR() ändern da PLZ auch mit 0 anfangen kann und führende 0 werden bei SQL
            ignoriert.
         */
        if (!postalCode.matches("\\d{5}")) {
            RulesService.showErrorAlert("Die Postleitzahl darf nur aus Zahlen bestehen und muss 5-stellig sein.");
            return false;
        }
        return true;
    }

    private  boolean isValidCountry(String country) {
        if(!country.matches("^[a-zA-Z]+$")) {
            RulesService.showErrorAlert("Bitte geben Sie ein gültiges Land ein.");
            return false;
        }
        return true;
    }

}
