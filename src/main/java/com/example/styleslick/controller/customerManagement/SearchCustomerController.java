package com.example.styleslick.controller.customerManagement;

import com.example.styleslick.model.CustomerService;
import com.example.styleslick.model.UserSession;

public class SearchCustomerController {

    UserSession userSession;


    public void initialize() {
         userSession = UserSession.getInstance();
    }


    private void executeSearchCustomer() {
        /* TODO: SearchCustomer Methode ausbessern !!
            - Prüfen wie viele eingaben getätigt wurden bsp. ob nur Name oder Name und Nachname eingegeben wurde.
            - CustomerService Methode aufrufen lassen um die Kunden zu finden.
         */


        CustomerService customerService = CustomerService.getInstance();
        customerService.searchCustomer("Column Name", "Wert");
    }

}
