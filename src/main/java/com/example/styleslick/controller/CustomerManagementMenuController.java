package com.example.styleslick.controller;

import com.example.styleslick.model.Customer;
import com.example.styleslick.service.CustomerService;
import com.example.styleslick.service.RulesService;
import com.example.styleslick.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class CustomerManagementMenuController implements Initializable {

    private CustomerService customerService;

    @FXML
    private TableView<Customer> tableView_customer;
    @FXML
    private TableColumn<Customer, String> column_username;
    @FXML
    private TableColumn<Customer, String> column_name;
    @FXML
    private TableColumn<Customer, String> column_lastName;
    @FXML
    private TableColumn<Customer, String> column_street;
    @FXML
    private TableColumn<Customer, Integer> column_plz;
    @FXML
    private TableColumn<Customer, String> column_ort;
    @FXML
    private TableColumn<Customer, String> column_platform;
    @FXML
    private TableColumn<Customer, Integer> column_customer_id;
    @FXML
    private TextField field_username;
    @FXML
    private TextField field_name;
    @FXML
    private TextField field_lastName;
    @FXML
    private TextField field_street;
    @FXML
    private TextField field_plz;
    @FXML
    private TextField field_ort;
    @FXML
    private TextField field_platform;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerService = CustomerService.getInstance();

        tableView_customer.getSelectionModel().selectedIndexProperty();

        executeShowAllCustomers();
    }


    private void executeShowAllCustomers() {
        column_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        column_street.setCellValueFactory(new PropertyValueFactory<>("street"));
        column_ort.setCellValueFactory(new PropertyValueFactory<>("ort"));
        column_plz.setCellValueFactory(new PropertyValueFactory<>("plz"));
        column_platform.setCellValueFactory(new PropertyValueFactory<>("platform"));
        column_customer_id.setCellValueFactory(new PropertyValueFactory<>("customer_id"));

        ObservableList<Customer> observableList = FXCollections.observableArrayList(customerService.getCustomers());
        tableView_customer.setItems(observableList);
    }


    private void executeAddCustomer() {
        Map<String, String> fields = new HashMap<>();

        fields.put("benutzername", field_username.getText());
        fields.put("name", field_name.getText());
        fields.put("nachname", field_lastName.getText());
        fields.put("strasse", field_street.getText());
        fields.put("plz", field_plz.getText());
        fields.put("ort", field_ort.getText());
        fields.put("gekauft_ueber", field_platform.getText());

        if (customerService.addCustomer(fields)) {
            field_username.clear();
            field_name.clear();
            field_lastName.clear();
            field_street.clear();
            field_ort.clear();
            field_platform.clear();
            field_plz.clear();
            executeShowAllCustomers();
        }
    }


    private void executeUpdateCustomer() {
        Map<String, String> fields = new HashMap<>();

        // Abrufen des ausgewählten Kunden
        Customer selectedCustomer = tableView_customer.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            RulesService.showErrorAlert("Bitte wählen Sie einen Kunden aus der Tabelle aus, um ihn zu bearbeiten.");
            return;
        }

        fields.put("benutzername", field_username.getText());
        fields.put("name", field_name.getText());
        fields.put("nachname", field_lastName.getText());
        fields.put("strasse", field_street.getText());
        fields.put("plz", field_plz.getText());
        fields.put("ort", field_ort.getText());
        fields.put("gekauft_ueber", field_platform.getText());

        // Kunden aus der Datenbank löschen
        if (customerService.updateCustomer(fields, selectedCustomer.getCustomer_id())) {
            field_username.clear();
            field_name.clear();
            field_lastName.clear();
            field_street.clear();
            field_plz.clear();
            field_ort.clear();
            field_platform.clear();
            executeShowAllCustomers();
        }
    }


    private void executeSearchCustomer() {
        Map<String, String> fields = new HashMap<>();

        fields.put("benutzername", field_username.getText());
        fields.put("name", field_name.getText());
        fields.put("nachname", field_lastName.getText());
        fields.put("strasse", field_street.getText());
        fields.put("plz", field_plz.getText());
        fields.put("ort", field_ort.getText());
        fields.put("gekauft_ueber", field_platform.getText());


        List<Customer> listOfCustomers = customerService.searchCustomer(fields);

        // Wenn kein Customer gefunden wurde abbrechen
        if (listOfCustomers == null || listOfCustomers.isEmpty()) {
            executeShowAllCustomers();
            return;
        }

        // Packt die Liste von Customers in die Tabellenansicht
        ObservableList<Customer> observableList = FXCollections.observableArrayList(listOfCustomers);
        tableView_customer.setItems(observableList);
    }


    public void executeDeleteCustomer() {
        // Abrufen des ausgewählten Kunden
        Customer selectedCustomer = tableView_customer.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            RulesService.showErrorAlert("Bitte wählen Sie einen Kunden aus der Tabelle aus, um ihn zu löschen.");
            return;
        }

        // Kunden aus der Datenbank löschen
        if (customerService.deleteCustomer(selectedCustomer.getCustomer_id())) {
            RulesService.showConfirmAlert("Kunde erfolgreich gelöscht.");
            executeShowAllCustomers();
        } else {
            RulesService.showErrorAlert("Kunde wurde nicht gelöscht.");
        }
    }


    private void executeExitCustomerManagement() {
        CustomerService.getInstance().clearSession();
        SceneManager.switchScene("/com/example/styleslick/loggedIn-view.fxml", "Willkommen");
    }

    @FXML
    private void onKeyPressedUpdateCustomer(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeUpdateCustomer();
        }
    }


    @FXML
    private void onMouseClickedUpdateCustomer(MouseEvent event) {
        executeUpdateCustomer();
    }


    @FXML
    private void onKeyPressedDeleteCustomer(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeDeleteCustomer();
        }
    }


    @FXML
    private void onMouseCLickedDeleteCustomer(MouseEvent event) {
        executeDeleteCustomer();
    }


    @FXML
    private void onKeyPressedEnterShowAllCustomers(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeShowAllCustomers();
        }
    }


    @FXML
    private void onMouseClickedShowAllCustomers(MouseEvent event) {
        executeShowAllCustomers();
    }


    @FXML
    private void onKeyPressedEnterSearchCustomer(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeSearchCustomer();
        }
    }

    @FXML
    private void onMouseClickedSearchCustomer(MouseEvent event) {
        executeSearchCustomer();
    }


    @FXML
    private void onKeyPressedEnterAddCustomer(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeAddCustomer();
        }
    }

    @FXML
    private void onMouseClickedAddCustomer(MouseEvent event) {
        executeAddCustomer();
    }


    @FXML
    private void onKeyPressedEnterExitCustomerManagement(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeExitCustomerManagement();
        }
    }

    @FXML
    private void onMouseClickedExitCustomerManagement(MouseEvent event) {
        executeExitCustomerManagement();
    }
}
