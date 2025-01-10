package com.example.styleslick.rules;

import com.example.styleslick.service.RulesService;

import java.util.Map;

public class CustomerRules {


    public static boolean isValidToAddCustomer(Map<String, String> filledFields) {

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

    private static boolean isValidPostalCode(String postalCode) {
        if (!postalCode.matches("\\d{5}")) {
            RulesService.showErrorAlert("Die Postleitzahl darf nur aus Zahlen bestehen und muss 5-stellig sein.");
            return false;
        }
        return true;
    }

    public static boolean isValidCountry(String country) {
        if(!country.matches("^[a-zA-Z]+$")) {
            RulesService.showErrorAlert("Bitte geben Sie ein gültiges Land ein.");
            return false;
        }
        return true;
    }

}
