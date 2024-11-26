package com.example.styleslick.model;

import com.example.styleslick.utils.SceneManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerService {

    private static CustomerService customerService;
    private Database database;


    private CustomerService() {}

    public static CustomerService getInstance() {
        if (customerService == null) {
            customerService = new CustomerService();
        }
        return customerService;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }



    public List<Customer> searchCustomer(String columnName1, String columnValue1,
                               String columnName2, String columnValue2,
                               String columnName3, String columnValue3,
                               String columnName4, String columnValue4,
                               String columnName5, String columnValue5,
                               String columnName6, String columnValue6,
                               String columnName7, String columnValue7) {

        // HashMap mit evtl. Leeren Eingaben
        Map<String, String> fields = new HashMap<>();

        // Alle Eingaben in die HashMap fields<> einsetzten
        fields.put(columnName1, columnValue1);
        fields.put(columnName2, columnValue2);
        fields.put(columnName3, columnValue3);
        fields.put(columnName4, columnValue4);
        fields.put(columnName5, columnValue5);
        fields.put(columnName6, columnValue6);
        fields.put(columnName7, columnValue7);

        // TODO: Man muss alle Eingaben ausfüllen damit man den Kunden findet.
        //  - FIXEN !! Man soll aber auch mit nur einer Eingabe Kunden finden können!
        //  - Evtl. in der SearchCustomerController bei der Methode executeSearchCustomer() schon abfangen ob die Eingaben
        //      Leer sind

        // HashMap für Werte die auch wirklich Eingegeben sind und nicht leer gelassen wurden
        Map<String, String> filledFields = new HashMap<>();

        // Alle ausgefüllten Felder von fields<> in filledFields<> Schreiben
        for (Map.Entry<String, String> entry : fields.entrySet()) {

            // Prüft welche Felder leer gelassen worden sind in fields<>
            if (entry.getValue() != null || !entry.getValue().trim().isEmpty()) {
                filledFields.put(entry.getKey(), entry.getValue());
            }
        }

        // Liste der gefundenen Kunden
        List<Customer> foundedCustomers = database.searchCustomer(filledFields);
        return foundedCustomers;



//        SceneManager.switchScene("path// foundCustomers-view.fxml", "Gefundene Kunden");
    }


}
